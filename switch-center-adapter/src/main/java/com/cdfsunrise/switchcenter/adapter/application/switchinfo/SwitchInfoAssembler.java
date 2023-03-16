package com.cdfsunrise.switchcenter.adapter.application.switchinfo;

import com.cdfsunrise.switchcenter.adapter.domain.switchinfo.SwitchInfo;
import com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoPo;
import org.springframework.stereotype.Component;

@Component
public class SwitchInfoAssembler {

    public SwitchInfoValueResponse toSwitchInfoValueResponse(SwitchInfo switchInfo) {
        SwitchInfoValueResponse response = new SwitchInfoValueResponse();
        response.setNs(switchInfo.getSwitchInfoKey().getNamespaceId());
        response.setKey(switchInfo.getSwitchInfoKey().getKey());
        response.setValue(switchInfo.activeValue());
        response.setStatus(switchInfo.isOn() ? "on" : "off");

        return response;
    }

    public SwitchInfoValueResponse toSwitchInfoValueResponse(SwitchInfoPo switchInfo) {
        SwitchInfoValueResponse response = new SwitchInfoValueResponse();
        response.setNs(switchInfo.getNamespaceId());
        response.setKey(switchInfo.getKey());
        response.setValue(switchInfo.getOn() ? switchInfo.getOnValue() : switchInfo.getOffValue());
        response.setStatus(switchInfo.getOn() ? "on" : "off");

        return response;
    }

}
