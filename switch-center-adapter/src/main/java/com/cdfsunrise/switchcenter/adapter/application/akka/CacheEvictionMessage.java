package com.cdfsunrise.switchcenter.adapter.application.akka;

import com.cdfsunrise.switchcenter.adapter.application.akka.codec.AkkaSerializable;
import com.cdfsunrise.switchcenter.adapter.driving.cache.SwitchCacheKey;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CacheEvictionMessage implements AkkaSerializable {
    private static final long serialVersionUID = -349299974620812921L;

    private SwitchCacheKey key;
    private List<SwitchCacheKey> children;

    private String sender;

    public CacheEvictionMessage(SwitchCacheKey key) {
        this(key, null);
    }

    public CacheEvictionMessage(SwitchCacheKey key, List<SwitchCacheKey> children) {
        this.key = key;
        this.children = children;
        this.sender = AkkaServerEnvironment.getEnv().getAkkaSystemPath();
    }
}
