package com.cdfsunrise.switchcenter.adapter.driving.cache;

import com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoPo;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class SwitchCacheManager {

    private Cache<SwitchCacheKey, SwitchInfoPo> cache = CacheBuilder.newBuilder()
            .concurrencyLevel(10)
            .maximumSize(50000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

}
