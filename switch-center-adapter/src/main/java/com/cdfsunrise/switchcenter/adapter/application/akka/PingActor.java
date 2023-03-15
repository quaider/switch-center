package com.cdfsunrise.switchcenter.adapter.application.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class PingActor extends AbstractActor {

    public static Props props() {
        return Props.create(PingActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Ping.class, m -> {
                    getSender().tell("pong", getSelf());
                })
                .build();
    }

}
