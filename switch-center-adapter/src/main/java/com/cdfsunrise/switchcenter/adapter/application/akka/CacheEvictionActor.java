package com.cdfsunrise.switchcenter.adapter.application.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class CacheEvictionActor extends AbstractActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public CacheEvictionActor() {
        ActorRef mediator = DistributedPubSub.get(getContext().system()).mediator();
        // subscribe to the topic named "content"
        mediator.tell(new DistributedPubSubMediator.Subscribe("cache-eviction", getSelf()), getSelf());
    }

    public static Props create() {
        return Props.create(CacheEvictionActor.class);
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
    }

}
