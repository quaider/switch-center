package com.cdfsunrise.switchcenter.adapter.domain.switchinfo;

import com.cdfsunrise.smart.framework.core.exception.BizDuplicatedException;
import com.cdfsunrise.smart.framework.core.exception.BizNotFoundException;
import com.cdfsunrise.smart.framework.core.exception.BizValidateException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SwitchInfoValidator {

    private final SwitchInfoRepository switchRepository;

    public void switchMustUnique(String namespaceId, String key) {
        if (switchRepository.isSwitchExist(namespaceId, key)) {
            throw new BizDuplicatedException("switch with key `{}` already exist", key);
        }
    }

    public SwitchInfo parentSwitchMustExist(String namespaceId, String parentKey) {
        return switchRepository.findByNamespaceAndKey(namespaceId, parentKey)
                .orElseThrow(() -> new BizNotFoundException("parent switch with key `{}` not found", parentKey));
    }

    public void switchKeyNameShouldNotEmpty(String key, String name) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(name)) {
            throw new BizValidateException("field in [key, name] is required");
        }
    }

    public void switchValueShouldNotEmptyAndSame(String key, String offValue, String onValue) {
        if (StringUtils.isEmpty(offValue) || StringUtils.isEmpty(onValue)) {
            throw new BizValidateException("switch value is required");
        }

        if (offValue.equalsIgnoreCase(onValue)) {
            throw new BizValidateException("the switch value on key `{}` must not be same", key);
        }
    }

    public void childSwitchKeyShouldExtendParent(String key, String parentKey) {
        if (!key.startsWith(parentKey + ".")) {
            throw new BizValidateException("child switch with key `{}` should start with parentKey", key);
        }
    }
}
