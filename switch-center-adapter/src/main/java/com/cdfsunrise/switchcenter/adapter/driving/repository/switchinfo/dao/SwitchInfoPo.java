package com.cdfsunrise.switchcenter.adapter.driving.repository.switchinfo.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cdfsunrise.smart.framework.mbp.persistence.IntBasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("switch_info")
public class SwitchInfoPo extends IntBasePo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String namespaceId;
    private String parentKey;
    @TableField(value = "`key`")
    private String key;
    private String name;
    private String description;

    @TableField(value = "`on`")
    private Boolean on;
    private String offValue;
    private String onValue;
}
