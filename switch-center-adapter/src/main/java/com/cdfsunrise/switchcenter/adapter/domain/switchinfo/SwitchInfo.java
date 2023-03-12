package com.cdfsunrise.switchcenter.adapter.domain.switchinfo;

import com.cdfsunrise.smart.framework.core.domain.AggregateRoot;
import com.cdfsunrise.smart.framework.core.exception.BizDuplicatedException;
import com.cdfsunrise.smart.framework.core.exception.BizForbiddenException;
import com.cdfsunrise.smart.framework.core.exception.BizValidateException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@Builder(setterPrefix = "set", toBuilder = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    private List<SwitchInfo> childSwitches = new ArrayList<>();

    public SwitchInfo(String namespaceId, String key) {
        if (StringUtils.isEmpty(namespaceId) || StringUtils.isEmpty(key)) {
            throw new BizValidateException("field in [namespaceId, key] is required");
        }

        this.parentKey = "";
        this.namespaceId = namespaceId;
        this.key = key;
    }

    public SwitchInfo(String namespaceId, String parentKey, String key) {
        if (StringUtils.isEmpty(namespaceId) || StringUtils.isEmpty(parentKey) || StringUtils.isEmpty(key)) {
            throw new BizValidateException("field in [namespaceId, parentKey, key] is required");
        }

        this.namespaceId = namespaceId;
        this.parentKey = parentKey;
        this.key = key;
    }

    public void initDisplay(String name, String description) {
        switchKeyNameShouldNotEmpty(key, name);

        this.name = name;
        this.description = description;
    }

    public void turnOff() {
        if (!this.isOn()) {
            throw new BizForbiddenException("switch `{}` is already turned off", qualifiedKey());
        }

        this.on = false;
        childSwitches.forEach(SwitchInfo::turnOff);
    }

    public void turnOn(SwitchInfo parent) {
        if (this.isOn()) {
            throw new BizForbiddenException("switch `{}` is already turned on", qualifiedKey());
        }

        if (parent != null && !parent.isOn()) {
            throw new BizForbiddenException("parent switch `{}` must be turned on", parent.qualifiedKey());
        }

        this.on = true;
    }

    public String activeValue() {
        return this.on ? onValue : offValue;
    }

    public void initSwitchValue(String offValue, String onValue) {
        switchValueShouldNotEmptyAndSame(key, offValue, onValue);

        this.offValue = offValue;
        this.onValue = onValue;
    }

    public SwitchInfo addChildSwitch(String key, String name, String description, String offValue, String onValue) {

        switchKeyNameShouldNotEmpty(key, name);
        switchShouldUnique(key);
        childSwitchKeyShouldExtendParent(key, this.key);
        switchValueShouldNotEmptyAndSame(key, offValue, onValue);

        SwitchInfo child = new SwitchInfo(this.namespaceId, this.key, key);
        child.description = description;
        child.name = name;
        child.offValue = offValue;
        child.onValue = onValue;

        childSwitches.add(child);

        return this;
    }

    public String qualifiedKey() {
        return String.format("%s.%s.%s", namespaceId, parentKey, key);
    }

    private void switchKeyNameShouldNotEmpty(String key, String name) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(name)) {
            throw new BizValidateException("field in [key, name] is required");
        }
    }

    private void switchShouldUnique(String key) {
        if (childSwitches.stream().anyMatch(f -> f.key.equals(key))) {
            throw new BizDuplicatedException("the switch with key `{}` is already exist", key);
        }
    }

    private void switchValueShouldNotEmptyAndSame(String key, String offValue, String onValue) {
        if (StringUtils.isEmpty(offValue) || StringUtils.isEmpty(onValue)) {
            throw new BizValidateException("switch value is required");
        }

        if (offValue.equalsIgnoreCase(onValue)) {
            throw new BizValidateException("the switch value on key `{}` must not be same", key);
        }
    }

    private void childSwitchKeyShouldExtendParent(String key, String parentKey) {
        if (!key.startsWith(parentKey + ".")) {
            throw new BizValidateException("child switch with key `{}` should start with parentKey", key);
        }
    }
}
