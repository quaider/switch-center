package com.cdfsunrise.switchcenter.adapter.application.akka;

import akka.pattern.AskTimeoutException;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cdfsunrise.smart.framework.core.exception.BizForbiddenException;
import com.cdfsunrise.smart.framework.mbp.audit.AutoFillAudit;
import com.cdfsunrise.switchcenter.adapter.driving.repository.server.dao.ServerInfoMapper;
import com.cdfsunrise.switchcenter.adapter.driving.repository.server.dao.ServerInfoPo;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServerService {

    private final ServerInfoMapper serverInfoMapper;

    public ServerInfoPo findByIpAndPort(String ip, int port) {
        return serverInfoMapper.selectOne(buildWrapper(ip, port));
    }

    public List<ServerInfoPo> getCandidateSeedServers() {
        Date healthEndTime = DateUtils.addHours(new Date(), -1);

        LambdaQueryWrapper<ServerInfoPo> queryWrapper = Wrappers.<ServerInfoPo>lambdaQuery()
                .gt(ServerInfoPo::getUpdateTime, healthEndTime)
                .orderByDesc(ServerInfoPo::getUpdateTime);

        return serverInfoMapper.selectList(queryWrapper);
    }

    public List<ServerInfoPo> electSeedServer() {
        List<ServerInfoPo> candidateSeedServers = getCandidateSeedServers();
        if (ObjectUtils.isEmpty(candidateSeedServers)) {
            return Lists.newArrayList(currentServer());
        }

        List<ServerInfoPo> healthServer = new ArrayList<>();

        AkkaServerEnvironment env = AkkaServerEnvironment.getEnv();
        for (ServerInfoPo serverInfoPo : candidateSeedServers) {
            String pingPath = env.getActorPath(serverInfoPo.getIp(), serverInfoPo.getPort(), "ping");
            CompletableFuture<Object> result = (CompletableFuture<Object>) akka.pattern.Patterns.ask(
                    env.getActorSystem().actorSelection(pingPath),
                    new Ping(),
                    Duration.ofMillis(3000)
            );

            try {
                Object r = result.get();
                healthServer.add(serverInfoPo);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                if (e.getCause() instanceof AskTimeoutException) {
                    log.warn("server is down, server={}", JSON.toJSONString(serverInfoPo));
                }
            }
        }

        return null;
    }

    @Scheduled(fixedDelay = 30 * 1000)
    public void heartbeat() {
        AkkaServerEnvironment akkaEnv = AkkaServerEnvironment.getEnv();
        ServerInfoPo serverInfoPo = findByIpAndPort(akkaEnv.getServerIp(), akkaEnv.getServerPort());
        if (serverInfoPo == null) {
            serverInfoMapper.insert(currentServer());
        } else {
            ServerInfoPo serverInfoPoUpdater = new ServerInfoPo();
            serverInfoPoUpdater.setId(serverInfoPo.getId());
            serverInfoMapper.updateById(serverInfoPoUpdater);
        }
    }

    private ServerInfoPo currentServer() {
        AkkaServerEnvironment akkaEnv = AkkaServerEnvironment.getEnv();
        if (StringUtils.isEmpty(akkaEnv.getServerIp())) {
            throw new BizForbiddenException("");
        }

        ServerInfoPo serverInfoPo = new ServerInfoPo();
        serverInfoPo.setPort(akkaEnv.getServerPort());
        serverInfoPo.setIp(akkaEnv.getServerIp());

        return serverInfoPo;
    }

    /**
     * 超过1天仍然没有上报过心跳的服务器
     */
    @Scheduled(fixedDelay = 2 * 3600 * 1000)
    public void removeExpiredServers() {
        Date yesterday = DateUtils.addDays(new Date(), -1);

        AkkaServerEnvironment akkaEnv = AkkaServerEnvironment.getEnv();

        LambdaQueryWrapper<ServerInfoPo> queryWrapper = buildWrapper(akkaEnv.getServerIp(), akkaEnv.getServerPort())
                .le(AutoFillAudit::getUpdateTime, yesterday);

        int rows = serverInfoMapper.delete(queryWrapper);

        if (rows > 0) {
            log.warn("removed {} expired servers", rows);
        }
    }

    private LambdaQueryWrapper<ServerInfoPo> buildWrapper(String ip, int port) {
        return Wrappers.<ServerInfoPo>lambdaQuery()
                .eq(ServerInfoPo::getIp, ip)
                .eq(ServerInfoPo::getPort, port);
    }

}
