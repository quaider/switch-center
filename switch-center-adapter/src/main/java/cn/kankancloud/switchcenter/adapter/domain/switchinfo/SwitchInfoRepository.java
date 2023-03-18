package cn.kankancloud.switchcenter.adapter.domain.switchinfo;

import cn.kankancloud.jbp.core.domain.IRepository;

import java.util.Optional;

public interface SwitchInfoRepository extends IRepository<Integer, SwitchInfo> {
    Optional<SwitchInfo> findByNamespaceAndKey(String namespaceId, String key);

    boolean isSwitchExist(String namespaceId, String key);

    boolean containsSwitch(String namespaceId);
}
