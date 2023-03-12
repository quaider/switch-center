package com.cdfsunrise.switchcenter.adapter.domain.switchinfo;

import com.cdfsunrise.smart.framework.core.domain.IRepository;

import java.util.Optional;

public interface SwitchInfoRepository extends IRepository<Integer, SwitchInfo> {
    Optional<SwitchInfo> findByNamespaceAndKey(String namespaceId, String key);

    boolean isSwitchExist(String namespaceId, String key);

    boolean containsSwitch(String namespaceId);
}
