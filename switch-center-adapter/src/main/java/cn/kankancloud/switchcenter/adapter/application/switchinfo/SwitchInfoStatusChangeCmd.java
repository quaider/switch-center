package cn.kankancloud.switchcenter.adapter.application.switchinfo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SwitchInfoStatusChangeCmd implements Serializable {

    private static final long serialVersionUID = -5744499026283472674L;

    @NotNull
    private Boolean turnOn;
}
