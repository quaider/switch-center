package cn.kankancloud.switchcenter.adapter.driving.repository.namespace.dao;

import cn.kankancloud.jbp.mbp.persistence.IntBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
