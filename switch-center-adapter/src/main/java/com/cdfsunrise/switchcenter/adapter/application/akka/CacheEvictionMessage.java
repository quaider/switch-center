package com.cdfsunrise.switchcenter.adapter.application.akka;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CacheEvictionMessage implements Serializable {
    private static final long serialVersionUID = -349299974620812921L;

    private String namespaceId;
    private String key;

    public CacheEvictionMessage(String namespaceId, String key) {
        this.namespaceId = namespaceId;
        this.key = key;
    }
}
