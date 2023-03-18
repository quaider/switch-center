package cn.kankancloud.switchcenter.adapter.application.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import cn.kankancloud.switchcenter.adapter.driving.cache.SwitchCacheManager;
import org.apache.commons.lang3.ObjectUtils;

public class CacheEvictionActor extends AbstractActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final SwitchCacheManager switchCacheManager;

    public CacheEvictionActor(SwitchCacheManager switchCacheManager) {
        this.switchCacheManager = switchCacheManager;
        ActorRef mediator = DistributedPubSub.get(getContext().system()).mediator();
        // subscribe to the topic named "content"
        mediator.tell(new DistributedPubSubMediator.Subscribe("cache-eviction", getSelf()), getSelf());
    }

    public static Props create(SwitchCacheManager cacheManager) {
        return Props.create(CacheEvictionActor.class, cacheManager);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        CacheEvictionMessage.class,
                        this::onCacheEviction
                )
                .build();
    }

    private void onCacheEviction(CacheEvictionMessage message) {
        log.info("================cache eviction begin====namespace:{}, key:{}=================", message.getKey().getNamespace(), message.getKey().getKey());
        switchCacheManager.evict(message.getKey());

        if (ObjectUtils.isNotEmpty(message.getChildren())) {
            message.getChildren().forEach(switchCacheManager::evict);
        }
    }

}
