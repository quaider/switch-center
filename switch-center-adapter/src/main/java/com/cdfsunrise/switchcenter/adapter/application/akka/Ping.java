package com.cdfsunrise.switchcenter.adapter.application.akka;

import lombok.Data;

import java.io.Serializable;

@Data
public class Ping implements Serializable {
    private static final long serialVersionUID = 8706736920816653477L;

    private Long timestamp;

    public Ping() {
        timestamp = System.currentTimeMillis();
    }

    public Ping(Long timestamp) {
        this.timestamp = timestamp;
    }
}
