package cn.kankancloud.switchcenter.adapter.domain.switchinfo;

import cn.kankancloud.jbp.core.domain.AggregateRoot;
import cn.kankancloud.jbp.core.exception.BizForbiddenException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@EqualsAndHashCode(callSuper = true)
public class SwitchInfo extends AggregateRoot<Integer, SwitchInfo> {

    /**
     * 没有实际作用，只是为了给给orm或mybatis使用
     */
    @Setter
    private Integer id;

    private final SwitchInfoKey switchInfoKey;

    private SwitchInfoDescription description;

    private SwitchInfoValue value;

    private boolean on = false;

    public SwitchInfo(String namespaceId, String parentKey, String key) {
        this.switchInfoKey = new SwitchInfoKey(namespaceId, parentKey, key);
    }

    public SwitchInfo(SwitchInfoKey switchInfoKey, SwitchInfoDescription description, SwitchInfoValue value, boolean on) {
        if (switchInfoKey == null || description == null || value == null) {
            throw new IllegalArgumentException("switchKey or description or value is required");
        }

        this.switchInfoKey = switchInfoKey;
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
        return switchInfoKey.qualifiedKey();
    }

    public boolean isParent() {
        return StringUtils.isEmpty(switchInfoKey.getParentKey());
    }
}
