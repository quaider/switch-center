package com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo;

import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfo;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfoDescription;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfoKey;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfoValue;
import com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoPo;

public class SwitchInfoConverter {

    private SwitchInfoConverter() {
    }

    public static SwitchInfoPo toPo(SwitchInfo switchInfo) {
        SwitchInfoPo switchInfoPo = new SwitchInfoPo();
        switchInfoPo.setId(switchInfo.getId());
        switchInfoPo.setNamespaceId(switchInfo.getSwitchInfoKey().getNamespaceId());
        switchInfoPo.setParentKey(switchInfo.getSwitchInfoKey().getParentKey());
        switchInfoPo.setKey(switchInfo.getSwitchInfoKey().getKey());
        switchInfoPo.setName(switchInfo.getDescription().getName());
        switchInfoPo.setDescription(switchInfo.getDescription().getDescription());
        switchInfoPo.setOn(switchInfo.isOn());
        switchInfoPo.setOffValue(switchInfo.getValue().getOnValue());
        switchInfoPo.setOnValue(switchInfo.getValue().getOffValue());

        return switchInfoPo;
    }

    public static SwitchInfo toDomain(SwitchInfoPo po) {
        if (po == null) {
            return null;
        }

        return new SwitchInfo(
                new SwitchInfoKey(po.getNamespaceId(), po.getParentKey(), po.getKey()),
                new SwitchInfoDescription(po.getName(), po.getDescription()),
                new SwitchInfoValue(po.getOffValue(), po.getOnValue()),
                Boolean.TRUE.equals(po.getOn())
        ).onPersisted(po.getId());
    }

}
