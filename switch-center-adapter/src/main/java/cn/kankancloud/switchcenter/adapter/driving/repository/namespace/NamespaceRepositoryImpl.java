package cn.kankancloud.switchcenter.adapter.driving.repository.namespace;

import cn.kankancloud.jbp.core.domain.BaseRepository;
import cn.kankancloud.switchcenter.adapter.domain.namespace.Namespace;
import cn.kankancloud.switchcenter.adapter.domain.namespace.NamespaceRepository;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.kankancloud.switchcenter.adapter.driving.repository.namespace.dao.NamespaceMapper;
import cn.kankancloud.switchcenter.adapter.driving.repository.namespace.dao.NamespacePo;
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
    protected Integer saveInternal(Namespace ns) {
        if (ns.entityStatus().isUnchanged()) {
            return ns.getId();
        }

        NamespacePo nsPo = NamespaceConverter.toPo(ns);

        if (ns.entityStatus().isNew()) {
            nsPo.setNamespaceId(ns.getNamespaceId());
            namespaceMapper.insert(nsPo);
            return nsPo.getId();
        }

        if (ns.entityStatus().isUpdated()) {
            // do not update
            nsPo.setNamespaceId(null);
            return namespaceMapper.updateById(nsPo);
        }

        return namespaceMapper.deleteById(ns.getId());
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
