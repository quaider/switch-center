package com.cdfsunrise.switchcenter.adapter.application.switchinfo;

import com.cdfsunrise.smart.framework.core.exception.BizNotFoundException;
import com.cdfsunrise.switchcenter.adapter.domain.namespace.NamespaceValidator;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfo;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfoBuilder;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfoRepository;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SwitchInfoService {

    private final SwitchInfoRepository switchRepository;
    private final NamespaceValidator namespaceValidator;

    private final SwitchInfoValidator switchInfoValidator;

    private final SwitchInfoBuilder switchInfoBuilder;

    @Transactional(rollbackFor = Throwable.class)
    public void addSwitch(SwitchInfoCmd cmd) {

        SwitchInfo switchInfo = switchInfoBuilder
                .namespaceId(cmd.getNamespaceId())
                .parentKey(cmd.getParentKey())
                .key(cmd.getKey())
                .on(false)
                .onValue(cmd.getOnValue())
                .offValue(cmd.getOffValue())
                .name(cmd.getName())
                .description(cmd.getDescription())
                .build();

        switchRepository.save(switchInfo);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void changeStatus(String namespaceId, String key, SwitchInfoStatusChangeCmd cmd) {
        namespaceValidator.namespaceMustExist(namespaceId);

        Optional<SwitchInfo> switchInfoOpt = switchRepository.findByNamespaceAndKey(namespaceId, key);
        if (!switchInfoOpt.isPresent()) {
            throw new BizNotFoundException("the switch with key `{}` in namespace `{}` not found", key, namespaceId);
        }

        SwitchInfo switchInfo = switchInfoOpt.get();
        SwitchInfo parent = null;

        if (switchInfo.isParent()) {
            parent = switchInfoValidator.parentSwitchMustExist(namespaceId, switchInfo.getParentKey());
        }

        if (Boolean.TRUE.equals(cmd.getTurnOn())) {
            switchInfo.turnOn(parent);
        } else {
            switchInfo.turnOff();
        }

        switchInfo.toUpdate();

        switchRepository.save(switchInfo);
    }

}
