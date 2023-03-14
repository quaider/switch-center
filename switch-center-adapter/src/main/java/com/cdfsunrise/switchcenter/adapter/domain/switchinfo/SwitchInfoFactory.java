package com.cdfsunrise.switchcenter.adapter.domain.switchinfo;

import com.cdfsunrise.smart.framework.core.domain.DomainInjector;
import com.cdfsunrise.switchcenter.adapter.domain.namespace.NamespaceValidator;
import lombok.RequiredArgsConstructor;

@DomainInjector
@RequiredArgsConstructor
public class SwitchInfoFactory {
    private final NamespaceValidator namespaceValidator;
    private final SwitchInfoValidator switchInfoValidator;

    public SwitchInfo create(SwitchInfoKey switchInfoKey, SwitchInfoDescription description, SwitchInfoValue value) {
        namespaceValidator.namespaceMustExist(switchInfoKey.getNamespaceId());
        if (switchInfoKey.isParent()) {
            switchInfoValidator.parentSwitchMustExist(switchInfoKey.getNamespaceId(), switchInfoKey.getParentKey());
        }

        switchInfoValidator.switchMustUnique(switchInfoKey.getNamespaceId(), switchInfoKey.getKey());

        return new SwitchInfo(switchInfoKey, description, value, false);
    }

}
