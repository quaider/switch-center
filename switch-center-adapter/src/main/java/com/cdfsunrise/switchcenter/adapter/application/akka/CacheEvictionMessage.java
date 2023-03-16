package com.cdfsunrise.switchcenter.adapter.application.akka;

import com.cdfsunrise.switchcenter.adapter.application.akka.codec.AkkaSerializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CacheEvictionMessage implements AkkaSerializable {
    private static final long serialVersionUID = -349299974620812921L;

    private String namespaceId;
    private String key;

    private String sender;

    public CacheEvictionMessage(String namespaceId, String key) {
        this.namespaceId = namespaceId;
        this.key = key;
        this.sender = AkkaServerEnvironment.getEnv().getAkkaSystemPath();
    }
}
