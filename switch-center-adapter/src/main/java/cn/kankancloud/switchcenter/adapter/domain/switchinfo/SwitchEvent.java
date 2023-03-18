package cn.kankancloud.switchcenter.adapter.domain.switchinfo;

import cn.kankancloud.jbp.core.domain.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


public interface SwitchEvent {

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true, exclude = {"switchInfoKey", "childKeys"})
    class Deleted extends DomainEvent {
        private static final long serialVersionUID = 681693047480019829L;

        private SwitchInfoKey switchInfoKey;
        private List<SwitchInfoKey> childKeys;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    class Added extends DomainEvent {
        private SwitchInfo switchInfo;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    class Updated extends DomainEvent {
        private SwitchInfo switchInfo;
    }


}
