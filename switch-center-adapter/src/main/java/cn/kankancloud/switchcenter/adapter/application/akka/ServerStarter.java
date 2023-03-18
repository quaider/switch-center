package cn.kankancloud.switchcenter.adapter.application.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.cluster.Cluster;
import cn.kankancloud.switchcenter.adapter.driving.cache.SwitchCacheManager;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServerStarter implements ApplicationListener<ContextRefreshedEvent>, DisposableBean {
    private final ServerDiscovery serverDiscovery;
    private final SwitchCacheManager cacheManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        AkkaServerEnvironment env = AkkaServerEnvironment.getEnv();
        env.init();

        Map<String, Object> overrides = new HashMap<>();
        overrides.put("akka.remote.artery.canonical.hostname", env.getServerIp());
        overrides.put("akka.remote.artery.canonical.port", env.getServerPort());

        Config config = ConfigFactory.parseMap(overrides)
                .withFallback(ConfigFactory.load("akka-application.conf"));
        ActorSystem actorSystem = ActorSystem.create(AkkaServerEnvironment.ACTOR_SYS_NAME, config);
        env.setActorSystem(actorSystem);

        actorSystem.actorOf(PingActor.create(), "ping");
        ActorRef cacheEvictionPublisherActor = actorSystem.actorOf(CacheEvictionPublishActor.create(), "cacheEvictionPublisher");
        ActorRef cacheEvictionActor = actorSystem.actorOf(CacheEvictionActor.create(cacheManager), "cacheEviction");

        serverDiscovery.heartbeat();

        log.info("^.^ akka server {} started", env.getAkkaSystemPath());

        Cluster cluster = Cluster.get(actorSystem);
        env.setCluster(cluster);
        env.setPublisher(cacheEvictionPublisherActor);

        List<Address> list = serverDiscovery.discoverySeedNodes(2).stream()
                .map(f -> Address.apply("akka", AkkaServerEnvironment.ACTOR_SYS_NAME, f.getIp(), f.getPort()))
                .collect(Collectors.toList());

        if (list.isEmpty()) {
            cluster.join(cluster.selfAddress());
        } else {
            cluster.joinSeedNodes(list);
        }

        // subscribe CacheEvictionMessage
        actorSystem.getEventStream().subscribe(cacheEvictionActor, CacheEvictionMessage.class);
    }

    @Override
    public void destroy() {
        AkkaServerEnvironment env = AkkaServerEnvironment.getEnv();
        if (env.getActorSystem() == null) {
            return;
        }

        log.info("shutdown akka actor system {}", env.getAkkaSystemPath());
        env.getActorSystem().terminate();

        try {
            log.info("deleting current akka server seed {}", env.getAkkaSystemPath());
            serverDiscovery.removeCurrentServer();
        } catch (Exception e) {
            log.warn("delete akka current server {} failed", env.getAkkaSystemPath(), e);
        }
    }
}
