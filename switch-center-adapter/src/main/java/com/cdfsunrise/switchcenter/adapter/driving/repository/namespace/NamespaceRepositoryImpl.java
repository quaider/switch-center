package com.cdfsunrise.switchcenter.adapter.driving.repository.namespace;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cdfsunrise.smart.framework.core.domain.BaseRepository;
import com.cdfsunrise.smart.framework.core.domain.EntityStatus;
import com.cdfsunrise.switchcenter.adapter.domain.namespace.Namespace;
import com.cdfsunrise.switchcenter.adapter.domain.namespace.NamespaceRepository;
import com.cdfsunrise.switchcenter.adapter.driving.repository.namespace.dao.NamespaceMapper;
import com.cdfsunrise.switchcenter.adapter.driving.repository.namespace.dao.NamespacePo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
class NamespaceRepositoryImpl extends BaseRepository<Integer, Namespace> implements NamespaceRepository {

    private final NamespaceMapper namespaceMapper;

    @Override
    protected void saveInternal(Namespace ns) {
        if (ns.entityStatus() == EntityStatus.UNCHANGED) {
            return;
        }

        NamespacePo nsPo = NamespaceConverter.toPo(ns);

        if (ns.entityStatus() == EntityStatus.NEW) {
            nsPo.setNamespaceId(ns.getNamespaceId());
            namespaceMapper.insert(nsPo);
            ns.onPersisted(nsPo.getId());
            return;
        }

        if (ns.entityStatus() == EntityStatus.UPDATED) {
            // do not update
            nsPo.setNamespaceId(null);
            namespaceMapper.updateById(nsPo);
            return;
        }

        namespaceMapper.deleteById(ns.getId());
    }

    @Override
    public Optional<Namespace> findByNamespaceId(String namespaceId) {
        LambdaQueryWrapper<NamespacePo> queryWrapper = Wrappers.<NamespacePo>lambdaQuery().eq(NamespacePo::getNamespaceId, namespaceId);
        NamespacePo namespacePo = namespaceMapper.selectOne(queryWrapper);

        return Optional.ofNullable(namespacePo).map(NamespaceConverter::toDomain).map(f -> {
            f.toUnChange();
            return f;
        });
    }

    @Override
    public Optional<Namespace> findById(Integer id) {
        return Optional.ofNullable(namespaceMapper.selectById(id)).map(NamespaceConverter::toDomain);
    }
}
