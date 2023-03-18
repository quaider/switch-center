package cn.kankancloud.switchcenter.adapter.application.switchinfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SwitchInfoResponse implements Serializable {
    private static final long serialVersionUID = -3517690877955253074L;

    private Integer id;

    private String namespaceId;
    private String parentKey;
    private String key;
    private String name;
    private String description;
    private Boolean on;
    private String offValue;
    private String onValue;
}
