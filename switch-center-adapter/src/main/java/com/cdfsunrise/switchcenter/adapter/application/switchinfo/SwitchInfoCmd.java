package com.cdfsunrise.switchcenter.adapter.application.switchinfo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SwitchInfoCmd {

    private String parentKey;
    private String key;
    private String namespaceId;
    private String name;
    private String offValue;
    private String onValue;
    private String description;

    public SwitchInfoCmd(Add cmd, String namespaceId) {
        this(cmd, namespaceId, "");
    }

    public SwitchInfoCmd(Add cmd, String namespaceId, String parentKey) {
        this.key = cmd.getKey();
        this.name = cmd.getName();
        this.offValue = cmd.getOffValue();
        this.onValue = cmd.getOnValue();
        this.description = cmd.getDescription();
        this.namespaceId = namespaceId;
        this.parentKey = parentKey;
    }

    @Data
    public static class Add {
        @NotEmpty
        private String key;

        @NotEmpty
        private String name;

        @NotEmpty
        private String offValue = "off";

        @NotEmpty
        private String onValue = "on";

        private String description;
    }

}
