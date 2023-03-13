package com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo;

import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchDescription;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfo;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchKey;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchValue;
import com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoPo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SwitchInfoConverter {

    public SwitchInfoPo toPo(SwitchInfo switchInfo) {
        SwitchInfoPo switchInfoPo = new SwitchInfoPo();
        switchInfoPo.setId(switchInfo.getId());
        switchInfoPo.setNamespaceId(switchInfo.getSwitchKey().getNamespaceId());
        switchInfoPo.setParentKey(switchInfo.getSwitchKey().getParentKey());
        switchInfoPo.setKey(switchInfo.getSwitchKey().getKey());
        switchInfoPo.setName(switchInfo.getDescription().getName());
        switchInfoPo.setDescription(switchInfo.getDescription().getDescription());
        switchInfoPo.setOn(switchInfo.isOn());
        switchInfoPo.setOffValue(switchInfo.getValue().getOnValue());
        switchInfoPo.setOnValue(switchInfo.getValue().getOffValue());

        return switchInfoPo;
    }

    public SwitchInfo toDomain(SwitchInfoPo po) {
        return new SwitchInfo(
                new SwitchKey(po.getNamespaceId(), po.getParentKey(), po.getKey()),
                new SwitchDescription(po.getName(), po.getDescription()),
                new SwitchValue(po.getOffValue(), po.getOnValue()),
                Boolean.TRUE.equals(po.getOn())
        );
    }

}
