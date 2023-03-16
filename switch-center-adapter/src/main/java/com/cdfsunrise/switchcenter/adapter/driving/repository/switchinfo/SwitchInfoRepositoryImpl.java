package com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cdfsunrise.smart.framework.core.domain.BaseRepository;
import com.cdfsunrise.smart.framework.core.domain.EntityStatus;
import com.cdfsunrise.switchcenter.adapter.application.akka.AkkaServerEnvironment;
import com.cdfsunrise.switchcenter.adapter.application.akka.CacheEvictionMessage;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfo;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfoRepository;
import com.cdfsunrise.switchcenter.adapter.driving.cache.SwitchCacheKey;
import com.cdfsunrise.switchcenter.adapter.driving.cache.SwitchCacheManager;
import com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoMapper;
import com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoPo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class SwitchInfoRepositoryImpl extends BaseRepository<Integer, SwitchInfo> implements SwitchInfoRepository {

    private final SwitchInfoMapper switchInfoMapper;
    private final SwitchCacheManager switchCacheManager;

    @Override
    protected void saveInternal(SwitchInfo aggregate) {
        if (aggregate.entityStatus() == EntityStatus.DELETED) {
            deleteWithCascade(aggregate);
            return;
        }

        SwitchInfoPo switchPo = SwitchInfoConverter.toPo(aggregate);
        if (aggregate.entityStatus() != EntityStatus.UNCHANGED) {
            saveSinglePo(switchPo, aggregate.entityStatus());
        }
    }

    private void deleteWithCascade(SwitchInfo aggregate) {

        // 删除缓存
        switchCacheManager.evict(new SwitchCacheKey(aggregate.getSwitchInfoKey().getNamespaceId(), aggregate.getSwitchInfoKey().getKey()));

        switchInfoMapper.deleteById(aggregate.getId());

        // async: 延迟双删除
        AkkaServerEnvironment.getEnv().getPublisher().tell(new CacheEvictionMessage(aggregate.getSwitchInfoKey().getNamespaceId(), aggregate.getSwitchInfoKey().getKey()), null);

        if (StringUtils.isEmpty((aggregate.getSwitchInfoKey().getParentKey()))) {
            LambdaQueryWrapper<SwitchInfoPo> batchDeleteQuery = Wrappers.<SwitchInfoPo>lambdaQuery()
                    .eq(SwitchInfoPo::getNamespaceId, aggregate.getSwitchInfoKey().getNamespaceId())
                    .eq(SwitchInfoPo::getParentKey, aggregate.getSwitchInfoKey().getKey());

            switchInfoMapper.selectList(batchDeleteQuery).forEach(f -> {

                // 删除缓存
                switchCacheManager.evict(new SwitchCacheKey(f.getNamespaceId(), f.getKey()));

                switchInfoMapper.deleteById(f.getId());
                
                AkkaServerEnvironment.getEnv().getPublisher().tell(new CacheEvictionMessage(f.getNamespaceId(), f.getKey()), null);
            });
        }

    }

    private void saveSinglePo(SwitchInfoPo switchInfoPo, EntityStatus status) {
        if (status == EntityStatus.NEW) {
            switchInfoMapper.insert(switchInfoPo);
        } else if (status == EntityStatus.UPDATED) {
            switchInfoMapper.updateById(switchInfoPo);
        }

        switchCacheManager.addSwitch(switchInfoPo);
        // async: 延迟双删除
        AkkaServerEnvironment.getEnv().getPublisher().tell(new CacheEvictionMessage(switchInfoPo.getNamespaceId(), switchInfoPo.getKey()), null);
    }

    @Override
    public Optional<SwitchInfo> findByNamespaceAndKey(String namespaceId, String key) {
        LambdaQueryWrapper<SwitchInfoPo> queryWrapper = Wrappers.<SwitchInfoPo>lambdaQuery()
                .eq(SwitchInfoPo::getNamespaceId, namespaceId)
                .eq(SwitchInfoPo::getKey, key);

        SwitchInfoPo switchInfoPo = switchInfoMapper.selectOne(queryWrapper);

        return Optional.ofNullable(switchInfoPo).map(SwitchInfoConverter::toDomain);
    }

    @Override
    public boolean isSwitchExist(String namespaceId, String key) {
        LambdaQueryWrapper<SwitchInfoPo> queryWrapper = Wrappers.<SwitchInfoPo>lambdaQuery()
                .select(SwitchInfoPo::getId)
                .eq(SwitchInfoPo::getNamespaceId, namespaceId)
                .eq(SwitchInfoPo::getKey, key)
                .last("limit 1");

        return Optional.ofNullable(switchInfoMapper.selectOne(queryWrapper)).isPresent();
    }

    @Override
    public boolean containsSwitch(String namespaceId) {
        LambdaQueryWrapper<SwitchInfoPo> queryWrapper = Wrappers.<SwitchInfoPo>lambdaQuery()
                .select(SwitchInfoPo::getId)
                .eq(SwitchInfoPo::getNamespaceId, namespaceId)
                .last("limit 1");

        return Optional.ofNullable(switchInfoMapper.selectOne(queryWrapper)).isPresent();
    }

    @Override
    public Optional<SwitchInfo> findById(Integer id) {
        return null;
    }
}
