package cn.kankancloud.switchcenter.adapter.domain.switchinfo;

import cn.kankancloud.jbp.core.domain.ValueObject;
import cn.kankancloud.jbp.core.exception.BizValidateException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@EqualsAndHashCode(callSuper = true)
public class SwitchInfoValue extends ValueObject<SwitchInfoValue> {
    private final String offValue;
    private final String onValue;

    public SwitchInfoValue(String offValue, String onValue) {
        switchValueShouldNotEmptyAndSame(offValue, onValue);

        this.offValue = offValue;
        this.onValue = onValue;
    }

    public void switchValueShouldNotEmptyAndSame(String offValue, String onValue) {
        if (StringUtils.isEmpty(offValue) || StringUtils.isEmpty(onValue)) {
            throw new BizValidateException("switch value is required");
        }

        if (offValue.equalsIgnoreCase(onValue)) {
            throw new BizValidateException("the switch value must not be same");
        }
    }
}
