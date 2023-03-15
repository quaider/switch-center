package com.cdfsunrise.switchcenter.adapter.domain.switchinfo;

import com.cdfsunrise.smart.framework.core.domain.ValueObject;
import com.cdfsunrise.smart.framework.core.exception.BizValidateException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@EqualsAndHashCode(callSuper = true)
public class SwitchInfoKey extends ValueObject<SwitchInfoKey> {

    private final String namespaceId;
    private final String parentKey;
    private final String key;

    public SwitchInfoKey(String namespaceId, String parentKey, String key) {
        if (StringUtils.isEmpty(namespaceId) || parentKey == null || StringUtils.isEmpty(key)) {
            throw new BizValidateException("field in [namespaceId, parentKey, key] is required");
        }

        if (StringUtils.isNoneBlank(parentKey)) {
            childSwitchKeyShouldExtendParent(key, parentKey);
        }

        this.namespaceId = namespaceId;
        this.parentKey = parentKey;
        this.key = key;
    }

    private void childSwitchKeyShouldExtendParent(String key, String parentKey) {
        if (!key.startsWith(parentKey)) {
            throw new BizValidateException("child switch with key `{}` should start with parentKey", key);
        }
    }

    public boolean isParent() {
        return StringUtils.isNoneBlank(parentKey);
    }

    public String qualifiedKey() {
        return String.format("%s.%s.%s", namespaceId, parentKey, key);
    }
}
