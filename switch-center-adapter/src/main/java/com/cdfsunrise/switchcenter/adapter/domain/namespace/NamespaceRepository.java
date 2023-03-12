package com.cdfsunrise.switchcenter.adapter.domain.namespace;

import com.cdfsunrise.smart.framework.core.domain.IRepository;

import java.util.Optional;

public interface NamespaceRepository extends IRepository<Integer, Namespace> {
    Optional<Namespace> findByNamespaceId(String namespaceId);
}
