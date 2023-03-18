package cn.kankancloud.switchcenter.adapter.driving.repository.server.dao;

import cn.kankancloud.jbp.mbp.persistence.IntBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("server_info")
public class ServerInfoPo extends IntBasePo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String ip;

    private Integer port;
}
