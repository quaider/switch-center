package com.cdfsunrise.switchcenter.adapter.application.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ServerRegister implements ApplicationListener<ContextRefreshedEvent> {
    private final ServerService serverService;

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

        ActorRef ping = actorSystem.actorOf(PingActor.props(), "ping");
        System.out.println(ping.path().address().toString());

        env.setActorSystem(actorSystem);

        serverService.heartbeat();

        serverService.electSeedServer();
    }
}
