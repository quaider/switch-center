package com.cdfsunrise.switchcenter.adapter.domain.switchinfo;

import com.cdfsunrise.smart.framework.core.domain.DomainInjector;
import com.cdfsunrise.smart.framework.core.exception.BizDuplicatedException;
import com.cdfsunrise.smart.framework.core.exception.BizNotFoundException;
import lombok.RequiredArgsConstructor;

@DomainInjector
@RequiredArgsConstructor
public class SwitchInfoValidator {

    private final SwitchInfoRepository switchRepository;

    public void switchMustUnique(String namespaceId, String key) {
        if (switchRepository.isSwitchExist(namespaceId, key)) {
            throw new BizDuplicatedException("switch with key `{}` already exist", key);
        }
    }

    public SwitchInfo parentSwitchMustExist(String namespaceId, String parentKey) {
        return switchRepository.findByNamespaceAndKey(namespaceId, parentKey)
                .orElseThrow(() -> new BizNotFoundException("parent switch with key `{}` not found", parentKey));
    }
}
