package com.cdfsunrise.switchcenter.adapter.application.akka;

import com.cdfsunrise.switchcenter.adapter.application.akka.codec.AkkaSerializable;
import lombok.Data;

@Data
public class Ping implements AkkaSerializable {
    private static final long serialVersionUID = 8706736920816653477L;

    private Long timestamp;

    public Ping() {
        timestamp = System.currentTimeMillis();
    }

    public Ping(Long timestamp) {
        this.timestamp = timestamp;
    }
}
