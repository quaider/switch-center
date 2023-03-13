package com.cdfsunrise.switchcenter.adapter.domain.switchinfo;

import com.cdfsunrise.smart.framework.core.domain.AggregateRoot;
import com.cdfsunrise.smart.framework.core.exception.BizForbiddenException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@EqualsAndHashCode(callSuper = true)
public class SwitchInfo extends AggregateRoot<Integer, SwitchInfo> {

    @Setter
    private Integer id;

    private final SwitchKey switchKey;

    private SwitchDescription description;

    private SwitchValue value;

    private boolean on = false;

    public SwitchInfo(String namespaceId, String parentKey, String key) {
        this.switchKey = new SwitchKey(namespaceId, parentKey, key);
    }

    public SwitchInfo(SwitchKey switchKey, SwitchDescription description, SwitchValue value, boolean on) {
        if (switchKey == null || description == null || value == null) {
            throw new IllegalArgumentException("switchKey or description or value is required");
        }

        this.switchKey = switchKey;
        this.description = description;
        this.value = value;
        this.on = on;
    }

    public void turnOff() {
        if (!this.isOn()) {
            throw new BizForbiddenException("switch `{}` is already turned off", qualifiedKey());
        }

        this.on = false;
    }

    public void turnOn(SwitchInfo parent) {
        if (parent != null && !parent.isOn()) {
            throw new BizForbiddenException("parent switch `{}` must be turned on", parent.qualifiedKey());
        }

        if (this.isOn()) {
            throw new BizForbiddenException("switch `{}` is already turned on", qualifiedKey());
        }

        this.on = true;
    }

    public String activeValue() {
        return this.on ? value.getOnValue() : value.getOffValue();
    }

    public String qualifiedKey() {
        return switchKey.qualifiedKey();
    }

    public boolean isParent() {
        return StringUtils.isEmpty(switchKey.getParentKey());
    }
}
