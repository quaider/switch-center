package com.cdfsunrise.switchcenter.adapter.domain.switchinfo;

import com.cdfsunrise.switchcenter.adapter.domain.namespace.NamespaceValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Scope(scopeName = "prototype")
public class SwitchInfoBuilder {

    private final SwitchInfoValidator switchInfoValidator;
    private final NamespaceValidator namespaceValidator;

    private Integer id;
    private String namespaceId;
    private String parentKey;
    private String key;
    private String name;
    private String description;
    private boolean on;
    private String offValue;
    private String onValue;

    public SwitchInfoBuilder id(Integer id) {
        this.id = id;
        return this;
    }

    public SwitchInfoBuilder namespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
        return this;
    }

    public SwitchInfoBuilder parentKey(String parentKey) {
        this.parentKey = parentKey;
        return this;
    }

    public SwitchInfoBuilder key(String key) {
        this.key = key;
        return this;
    }

    public SwitchInfoBuilder name(String name) {
        this.name = name;
        return this;
    }

    public SwitchInfoBuilder description(String description) {
        this.description = description;
        return this;
    }

    public SwitchInfoBuilder on(boolean on) {
        this.on = on;
        return this;
    }

    public SwitchInfoBuilder offValue(String offValue) {
        this.offValue = offValue;
        return this;
    }

    public SwitchInfoBuilder onValue(String onValue) {
        this.onValue = onValue;
        return this;
    }

    public SwitchInfo build() {
        validate();

        return restore();
    }

    protected SwitchInfo restore() {
        SwitchInfo switchInfo = new SwitchInfo(namespaceId, parentKey, key);
        switchInfo.setOn(on);
        switchInfo.setName(name);
        switchInfo.setId(id);
        switchInfo.setOnValue(onValue);
        switchInfo.setOffValue(offValue);
        switchInfo.setDescription(description);

        return switchInfo;
    }

    private void validate() {
        namespaceValidator.namespaceMustExist(namespaceId);
        switchInfoValidator.switchKeyNameShouldNotEmpty(key, name);
        switchInfoValidator.switchMustUnique(namespaceId, key);
        if (StringUtils.isEmpty(parentKey)) {
            switchInfoValidator.parentSwitchMustExist(namespaceId, parentKey);
        }

        switchInfoValidator.childSwitchKeyShouldExtendParent(key, parentKey);
        switchInfoValidator.switchValueShouldNotEmptyAndSame(key, offValue, onValue);
    }


}

