package com.cdfsunrise.switchcenter.adapter.application.switchinfo;

import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.*;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.handler.StatusHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SwitchInfoService {

    private final SwitchInfoRepository switchRepository;
    private final SwitchFactory switchFactory;
    private final StatusHandler handler;

    @Transactional(rollbackFor = Throwable.class)
    public void addSwitch(SwitchInfoCmd cmd) {

        SwitchInfo switchInfo = switchFactory.create(
                new SwitchKey(cmd.getNamespaceId(), cmd.getParentKey(), cmd.getKey()),
                new SwitchDescription(cmd.getName(), cmd.getDescription()),
                new SwitchValue(cmd.getOffValue(), cmd.getOnValue())
        );

        switchRepository.save(switchInfo);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void changeStatus(String namespaceId, String key, SwitchInfoStatusChangeCmd cmd) {
        SwitchInfo switchInfo = handler.changeStatus(namespaceId, key, Boolean.TRUE.equals(cmd.getTurnOn()));

        switchRepository.save(switchInfo);
    }

}
