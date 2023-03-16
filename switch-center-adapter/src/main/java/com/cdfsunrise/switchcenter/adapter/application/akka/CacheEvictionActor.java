package com.cdfsunrise.switchcenter.adapter.application.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.cdfsunrise.switchcenter.adapter.driving.cache.SwitchCacheKey;
import com.cdfsunrise.switchcenter.adapter.driving.cache.SwitchCacheManager;

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
        log.info("================namespace:{}, key:{}=================", message.getKey(), message.getNamespaceId());
        switchCacheManager.evict(new SwitchCacheKey(message.getNamespaceId(), message.getKey()));
    }

}
