package com.cdfsunrise.switchcenter.adapter.application.switchinfo;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data
public class SwitchInfoStatusChangeCmd implements Serializable {

    private static final long serialVersionUID = -5744499026283472674L;

    @NonNull
    private Boolean turnOn;
}
