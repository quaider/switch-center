package com.cdfsunrise.switchcenter.adapter.driving.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoMapper;
import com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoPo;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class SwitchCacheManager {

    private final SwitchInfoMapper switchInfoMapper;
    private static Cache<SwitchCacheKey, Optional<SwitchInfoPo>> cache;

    private static final int CAPACITY = 50000;
    private static final int CONCURRENCY_LEVEL = 16;

    @SuppressWarnings("all")
    @PostConstruct
    public void init() {
        cache = CacheBuilder.newBuilder()
                .concurrencyLevel(CONCURRENCY_LEVEL)
                .maximumSize(CAPACITY)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(cacheLoader());
    }

    public Optional<SwitchInfoPo> getSwitch(SwitchCacheKey key) {
        try {
            return cache.get(key, () -> loadSwitch(key));
        } catch (ExecutionException e) {
            log.error("cache get error, key={}", key, e);
        }

        return Optional.empty();
    }

    public void addSwitch(SwitchInfoPo switchInfoPo) {
        if (switchInfoPo == null || switchInfoPo.getId() == null || switchInfoPo.getId() < 0) {
            throw new IllegalArgumentException("cache item must not be null and should not be a fake value");
        }

        SwitchCacheKey key = new SwitchCacheKey(switchInfoPo.getNamespaceId(), switchInfoPo.getKey());
        cache.put(key, Optional.of(switchInfoPo));
    }

    public void evict(SwitchCacheKey key) {
        cache.invalidate(key);
    }

    public Map<SwitchCacheKey, Optional<SwitchInfoPo>> readView() {
        return cache.asMap();
    }

    private CacheLoader<SwitchCacheKey, Optional<SwitchInfoPo>> cacheLoader() {
        return new CacheLoader<SwitchCacheKey, Optional<SwitchInfoPo>>() {
            @Override
            public Optional<SwitchInfoPo> load(SwitchCacheKey key) throws Exception {
                return loadSwitch(key);
            }
        };
    }

    /**
     * 缓存加载
     * 注：guava内部已经处理了并发冲突
     */
    private Optional<SwitchInfoPo> loadSwitch(SwitchCacheKey key) {
        LambdaQueryWrapper<SwitchInfoPo> wrapper = Wrappers.<SwitchInfoPo>lambdaQuery()
                .eq(SwitchInfoPo::getNamespaceId, key.getNamespace())
                .eq(SwitchInfoPo::getKey, key.getKey());

        return Optional.ofNullable(switchInfoMapper.selectOne(wrapper));
    }

}
