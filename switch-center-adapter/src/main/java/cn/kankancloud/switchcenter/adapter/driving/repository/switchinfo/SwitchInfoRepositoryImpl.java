package cn.kankancloud.switchcenter.adapter.driving.repository.switchinfo;

import cn.kankancloud.jbp.core.domain.BaseRepository;
import cn.kankancloud.jbp.core.domain.EntityStatus;
import cn.kankancloud.jbp.mbp.utils.ServiceProvider;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.kankancloud.switchcenter.adapter.domain.switchinfo.SwitchEvent;
import cn.kankancloud.switchcenter.adapter.domain.switchinfo.SwitchInfo;
import cn.kankancloud.switchcenter.adapter.domain.switchinfo.SwitchInfoKey;
import cn.kankancloud.switchcenter.adapter.domain.switchinfo.SwitchInfoRepository;
import cn.kankancloud.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoMapper;
import cn.kankancloud.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoPo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class SwitchInfoRepositoryImpl extends BaseRepository<Integer, SwitchInfo> implements SwitchInfoRepository {

    private final SwitchInfoMapper switchInfoMapper;

    @Override
    protected void saveInternal(SwitchInfo aggregate) {
        if (aggregate.entityStatus().isDeleted()) {
            deleteWithCascade(aggregate);
            return;
        }

        SwitchInfoPo switchPo = SwitchInfoConverter.toPo(aggregate);
        if (aggregate.entityStatus() != EntityStatus.UNCHANGED) {
            saveSinglePo(switchPo, aggregate);
        }
    }

    private void deleteWithCascade(SwitchInfo aggregate) {

        List<SwitchInfoKey> childKeys = new ArrayList<>();

        // 删除缓存
        switchInfoMapper.deleteById(aggregate.getId());

        if (StringUtils.isEmpty((aggregate.getSwitchInfoKey().getParentKey()))) {
            LambdaQueryWrapper<SwitchInfoPo> batchDeleteQuery = Wrappers.<SwitchInfoPo>lambdaQuery()
                    .eq(SwitchInfoPo::getNamespaceId, aggregate.getSwitchInfoKey().getNamespaceId())
                    .eq(SwitchInfoPo::getParentKey, aggregate.getSwitchInfoKey().getKey());

            childKeys = switchInfoMapper.selectList(batchDeleteQuery).stream()
                    .map(f -> new SwitchInfoKey(f.getNamespaceId(), f.getParentKey(), f.getKey()))
                    .collect(Collectors.toList());

            switchInfoMapper.delete(batchDeleteQuery);
        }

        ServiceProvider.getEventBus().publishEvent(new SwitchEvent.Deleted(aggregate.getSwitchInfoKey(), childKeys));
    }

    private void saveSinglePo(SwitchInfoPo switchInfoPo, SwitchInfo switchInfo) {
        if (switchInfo.entityStatus().isNew()) {
            switchInfoMapper.insert(switchInfoPo);
            switchInfo.onPersisted(switchInfo.getId());

            ServiceProvider.getEventBus().publishEvent(new SwitchEvent.Added(switchInfo));
        } else if (switchInfo.entityStatus().isUpdated()) {
            switchInfoMapper.updateById(switchInfoPo);

            ServiceProvider.getEventBus().publishEvent(new SwitchEvent.Updated(switchInfo));
        }
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
        SwitchInfoPo switchInfoPo = switchInfoMapper.selectById(id);

        return Optional.ofNullable(SwitchInfoConverter.toDomain(switchInfoPo));
    }
}
