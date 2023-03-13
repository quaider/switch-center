package com.cdfsunrise.switchcenter.adapter.domain.switchinfo;

import com.cdfsunrise.smart.framework.core.domain.AggregateRoot;
import com.cdfsunrise.smart.framework.core.exception.BizForbiddenException;
import com.cdfsunrise.smart.framework.core.exception.BizValidateException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@EqualsAndHashCode(callSuper = true)
@Setter(value = AccessLevel.PACKAGE)
public class SwitchInfo extends AggregateRoot<Integer, SwitchInfo> {

    @Setter(AccessLevel.PROTECTED)
    private Integer id;

    private final String namespaceId;
    private final String parentKey;
    private final String key;
    private String name;
    private String description;
    private boolean on = false;
    private String offValue = "off";
    private String onValue = "on";

    public SwitchInfo(String namespaceId, String parentKey, String key) {
        if (StringUtils.isEmpty(namespaceId) || parentKey == null || StringUtils.isEmpty(key)) {
            throw new BizValidateException("field in [namespaceId, parentKey, key] is required");
        }

        this.namespaceId = namespaceId;
        this.parentKey = parentKey;
        this.key = key;
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
        return this.on ? onValue : offValue;
    }

    public String qualifiedKey() {
        return String.format("%s.%s.%s", namespaceId, parentKey, key);
    }

    public boolean isParent() {
        return StringUtils.isEmpty(parentKey);
    }
}
