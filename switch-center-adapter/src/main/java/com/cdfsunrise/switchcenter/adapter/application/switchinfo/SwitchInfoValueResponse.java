package com.cdfsunrise.switchcenter.adapter.application.switchinfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SwitchInfoValueResponse implements Serializable {
    private static final long serialVersionUID = 5835332336933093061L;

    private String ns;

    private String key;

    private String value;

    private String status;

}
