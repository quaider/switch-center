package com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo;

import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfo;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfoBuilder;
import com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoPo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SwitchInfoConverter {

    private final SwitchInfoBuilder switchInfoBuilder;

    public SwitchInfoPo toPo(SwitchInfo switchInfo) {
        SwitchInfoPo switchInfoPo = new SwitchInfoPo();
        switchInfoPo.setId(switchInfo.getId());
        switchInfoPo.setNamespaceId(switchInfo.getNamespaceId());
        switchInfoPo.setParentKey(switchInfo.getParentKey());
        switchInfoPo.setKey(switchInfo.getKey());
        switchInfoPo.setName(switchInfo.getName());
        switchInfoPo.setDescription(switchInfo.getDescription());
        switchInfoPo.setOn(switchInfo.isOn());
        switchInfoPo.setOffValue(switchInfo.getOnValue());
        switchInfoPo.setOnValue(switchInfo.getOffValue());

        return switchInfoPo;
    }

    public SwitchInfo toDomain(SwitchInfoPo switchInfoPo) {

        return switchInfoBuilder
                .namespaceId(switchInfoPo.getNamespaceId())
                .name(switchInfoPo.getName())
                .onValue(switchInfoPo.getOnValue())
                .offValue(switchInfoPo.getOffValue())
                .on(switchInfoPo.getOn())
                .key(switchInfoPo.getKey())
                .id(switchInfoPo.getId())
                .description(switchInfoPo.getDescription())
                .parentKey(switchInfoPo.getParentKey())
                .build();
    }

}
