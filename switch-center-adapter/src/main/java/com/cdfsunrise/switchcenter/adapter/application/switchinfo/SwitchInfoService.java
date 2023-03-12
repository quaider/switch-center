package com.cdfsunrise.switchcenter.adapter.application.switchinfo;

import com.cdfsunrise.switchcenter.adapter.domain.namespace.NamespaceValidator;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfo;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfoRepository;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SwitchInfoService {

    private final SwitchInfoRepository switchRepository;
    private final SwitchInfoValidator switchValidator;
    private final NamespaceValidator namespaceValidator;

    @Transactional(rollbackFor = Throwable.class)
    public void addParentSwitch(SwitchInfoCmd cmd) {

        namespaceValidator.namespaceMustExist(cmd.getNamespaceId());
        switchValidator.switchMustUnique(cmd.getNamespaceId(), cmd.getKey());

        SwitchInfo switchInfo = new SwitchInfo(cmd.getNamespaceId(), cmd.getKey());
        switchInfo.initDisplay(cmd.getName(), cmd.getDescription());
        switchInfo.initSwitchValue(cmd.getOffValue(), cmd.getOnValue());

        switchRepository.save(switchInfo);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void addChildSwitch(SwitchInfoCmd cmd) {
        namespaceValidator.namespaceMustExist(cmd.getNamespaceId());
        SwitchInfo parent = switchValidator.parentSwitchMustExist(cmd.getNamespaceId(), cmd.getParentKey());
        switchValidator.switchMustUnique(cmd.getNamespaceId(), cmd.getKey());

        parent.addChildSwitch(cmd.getKey(), cmd.getName(), cmd.getDescription(), cmd.getOffValue(), cmd.getOnValue())
                .toUnChange();

        switchRepository.save(parent);
    }

}
