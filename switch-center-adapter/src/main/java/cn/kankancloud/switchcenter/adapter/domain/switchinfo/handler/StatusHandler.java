package cn.kankancloud.switchcenter.adapter.domain.switchinfo.handler;

import cn.kankancloud.jbp.core.domain.DomainInjector;
import cn.kankancloud.jbp.core.exception.BizNotFoundException;
import cn.kankancloud.switchcenter.adapter.domain.namespace.NamespaceValidator;
import cn.kankancloud.switchcenter.adapter.domain.switchinfo.SwitchInfo;
import cn.kankancloud.switchcenter.adapter.domain.switchinfo.SwitchInfoRepository;
import cn.kankancloud.switchcenter.adapter.domain.switchinfo.SwitchInfoValidator;
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

        if (!switchInfo.isParent()) {
            parent = switchInfoValidator.parentSwitchMustExist(namespaceId, switchInfo.getSwitchInfoKey().getParentKey());
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
