package cn.kankancloud.switchcenter.adapter.domain.namespace;

import cn.kankancloud.jbp.core.domain.IRepository;

import java.util.Optional;

public interface NamespaceRepository extends IRepository<Integer, Namespace> {
    Optional<Namespace> findByNamespaceId(String namespaceId);
}
