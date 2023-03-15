package com.cdfsunrise.switchcenter.adapter.driving.repository.server.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cdfsunrise.smart.framework.mbp.persistence.IntBasePo;
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
