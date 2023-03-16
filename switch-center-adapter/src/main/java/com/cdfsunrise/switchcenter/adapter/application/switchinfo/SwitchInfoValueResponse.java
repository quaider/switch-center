package com.cdfsunrise.switchcenter.adapter.application.switchinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwitchInfoValueResponse implements Serializable {
    private static final long serialVersionUID = 5835332336933093061L;

    private String ns;

    private String key;

    private String value;

    private String status;
}
