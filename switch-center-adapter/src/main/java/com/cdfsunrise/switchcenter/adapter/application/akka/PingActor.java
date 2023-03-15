package com.cdfsunrise.switchcenter.adapter.application.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class PingActor extends AbstractActor {

    public static Props create() {
        return Props.create(PingActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Ping.class, this::receivePing)
                .build();
    }

    private void receivePing(Ping ping) {
        long ts = Math.abs(System.currentTimeMillis() - ping.getTimestamp());
        if (ts > 5000) {
            getSender().tell("expired", getSelf());
        } else {
            getSender().tell("pong", getSelf());
        }
    }

}
