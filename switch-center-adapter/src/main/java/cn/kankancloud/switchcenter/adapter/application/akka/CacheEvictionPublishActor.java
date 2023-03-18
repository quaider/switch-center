package cn.kankancloud.switchcenter.adapter.application.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;

public class CacheEvictionPublishActor extends AbstractActor {

    public static Props create() {
        return Props.create(CacheEvictionPublishActor.class);
    }

    // activate the extension
    ActorRef mediator = DistributedPubSub.get(getContext().system()).mediator();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        CacheEvictionMessage.class,
                        message -> {
                            mediator.tell(new DistributedPubSubMediator.Publish("cache-eviction", message), getSelf());
                        })
                .build();
    }
}
