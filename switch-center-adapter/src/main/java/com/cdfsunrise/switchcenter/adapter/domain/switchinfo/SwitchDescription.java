package com.cdfsunrise.switchcenter.adapter.domain.switchinfo;

import com.cdfsunrise.smart.framework.core.domain.ValueObject;
import com.cdfsunrise.smart.framework.core.exception.BizValidateException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@EqualsAndHashCode(callSuper = true)
public class SwitchDescription extends ValueObject<SwitchDescription> {
    private final String name;
    private final String description;

    public SwitchDescription(String name, String description) {
        if (StringUtils.isEmpty(name)) {
            throw new BizValidateException("name is required");
        }

        this.name = name;
        this.description = description;
    }


}
