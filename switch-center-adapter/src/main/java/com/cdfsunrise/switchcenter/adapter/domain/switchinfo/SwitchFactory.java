package com.cdfsunrise.switchcenter.adapter.domain.switchinfo;

import com.cdfsunrise.smart.framework.core.domain.DomainInjector;
import com.cdfsunrise.switchcenter.adapter.domain.namespace.NamespaceValidator;
import lombok.RequiredArgsConstructor;

@DomainInjector
@RequiredArgsConstructor
public class SwitchFactory {
    private final NamespaceValidator namespaceValidator;
    private final SwitchInfoValidator switchInfoValidator;

    public SwitchInfo create(SwitchKey switchKey, SwitchDescription description, SwitchValue value) {
        namespaceValidator.namespaceMustExist(switchKey.getNamespaceId());
        if (switchKey.isParent()) {
            switchInfoValidator.parentSwitchMustExist(switchKey.getNamespaceId(), switchKey.getParentKey());
        }

        switchInfoValidator.switchMustUnique(switchKey.getNamespaceId(), switchKey.getKey());

        return new SwitchInfo(switchKey, description, value, false);
    }

}
