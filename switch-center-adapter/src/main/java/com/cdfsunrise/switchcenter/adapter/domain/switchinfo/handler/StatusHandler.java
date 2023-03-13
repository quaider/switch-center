package com.cdfsunrise.switchcenter.adapter.domain.switchinfo.handler;

import com.cdfsunrise.smart.framework.core.domain.DomainInjector;
import com.cdfsunrise.smart.framework.core.exception.BizNotFoundException;
import com.cdfsunrise.switchcenter.adapter.domain.namespace.NamespaceValidator;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfo;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfoRepository;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfoValidator;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@DomainInjector
@RequiredArgsConstructor
public class StatusHandler {

    private final NamespaceValidator namespaceValidator;
    private final SwitchInfoValidator switchInfoValidator;
    private final SwitchInfoRepository switchRepository;

    public SwitchInfo changeStatus(String namespaceId, String key, boolean turnOn) {
        namespaceValidator.namespaceMustExist(namespaceId);

        Optional<SwitchInfo> switchInfoOpt = switchRepository.findByNamespaceAndKey(namespaceId, key);
        if (!switchInfoOpt.isPresent()) {
            throw new BizNotFoundException("the switch with key `{}` in namespace `{}` not found", key, namespaceId);
        }

        SwitchInfo switchInfo = switchInfoOpt.get();
        SwitchInfo parent = null;

        if (switchInfo.isParent()) {
            parent = switchInfoValidator.parentSwitchMustExist(namespaceId, switchInfo.getSwitchKey().getParentKey());
        }

        if (turnOn) {
            switchInfo.turnOn(parent);
        } else {
            switchInfo.turnOff();
        }

        switchInfo.toUpdate();

        return switchInfo;
    }
}
