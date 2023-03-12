package com.cdfsunrise.switchcenter.adapter.driving.repository.namespace.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cdfsunrise.smart.framework.mbp.persistence.IntBasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("switch_namespace")
public class NamespacePo extends IntBasePo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String namespaceId;

    private String namespaceName;
}
