package com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo;

import com.cdfsunrise.smart.framework.core.util.tuple.Pair;
import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfo;
import com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoPo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class SwitchInfoConverter {

    private SwitchInfoConverter() {
    }

    public static Pair<SwitchInfoPo, List<SwitchInfoPo>> toPo(SwitchInfo switchInfo) {

        SwitchInfoPo switchInfoPo = toPoIgnoreChildren(switchInfo);
        List<SwitchInfoPo> children = switchInfo.getChildSwitches().stream()
                .map(SwitchInfoConverter::toPoIgnoreChildren)
                .collect(Collectors.toList());

        return Pair.with(switchInfoPo, children);
    }

    private static SwitchInfoPo toPoIgnoreChildren(SwitchInfo switchInfo) {
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

    public static SwitchInfo toDomain(SwitchInfoPo switchInfoPo, List<SwitchInfoPo> children) {
        SwitchInfo switchInfo;
        if (StringUtils.isEmpty(switchInfoPo.getParentKey())) {
            switchInfo = new SwitchInfo(switchInfoPo.getNamespaceId(), switchInfoPo.getKey());
        } else {
            switchInfo = new SwitchInfo(switchInfoPo.getNamespaceId(), switchInfoPo.getParentKey(), switchInfoPo.getKey());
        }

        switchInfo.initDisplay(switchInfoPo.getName(), switchInfoPo.getDescription());
        switchInfo.initSwitchValue(switchInfoPo.getOffValue(), switchInfoPo.getOnValue());

        switchInfo.onPersisted(switchInfoPo.getId());

        return switchInfo;
    }

}
