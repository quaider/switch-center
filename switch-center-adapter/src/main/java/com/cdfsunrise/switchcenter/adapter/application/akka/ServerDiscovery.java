package com.cdfsunrise.switchcenter.adapter.application.akka;

import akka.pattern.AskTimeoutException;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServerDiscovery {

    private final ServerInfoMapper serverInfoMapper;

    public ServerInfoPo findByIpAndPort(String ip, int port) {
        return serverInfoMapper.selectOne(buildWrapper(ip, port));
    }

    public List<ServerInfoPo> discoverySeedNodes(int size) {
        final ServerInfoPo currentServer = currentServer();
        List<ServerInfoPo> candidateSeedServers = getCandidateSeedServers().stream()
                .filter(f -> !f.getIp().equals(currentServer.getIp()) || !f.getPort().equals(currentServer.getPort()))
                .collect(Collectors.toList());

        if (ObjectUtils.isEmpty(candidateSeedServers)) {
            return Lists.newArrayList();
        }

        List<ServerInfoPo> healthServers = new ArrayList<>();

        AkkaServerEnvironment env = AkkaServerEnvironment.getEnv();
        for (ServerInfoPo serverInfoPo : candidateSeedServers) {

            // 约定最多返回2个种子节点
            if (healthServers.size() >= size) {
                return healthServers;
            }

            // ping target actor
            String pingPath = AkkaServerEnvironment.buildActorPath(serverInfoPo.getIp(), serverInfoPo.getPort(), "ping");
            CompletableFuture<Object> future = (CompletableFuture<Object>) akka.pattern.Patterns.ask(
                    env.getActorSystem().actorSelection(pingPath),
                    new Ping(),
                    Duration.ofMillis(3000)
            );

            try {
                String result = (String) future.get();
                if ("pong".equals(result)) {
                    healthServers.add(serverInfoPo);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                if (e.getCause() instanceof AskTimeoutException) {
                    log.warn("server {}:{} is unhealthy", serverInfoPo.getIp(), serverInfoPo.getPort());
                }
            }
        }

        return healthServers;
    }

    private List<ServerInfoPo> getCandidateSeedServers() {
        Date healthEndTime = DateUtils.addHours(new Date(), -1);

        LambdaQueryWrapper<ServerInfoPo> queryWrapper = Wrappers.<ServerInfoPo>lambdaQuery()
                .gt(ServerInfoPo::getUpdateTime, healthEndTime)
                .orderByAsc(ServerInfoPo::getUpdateTime);

        return serverInfoMapper.selectList(queryWrapper);
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

    public void removeCurrentServer() {
        AkkaServerEnvironment env = AkkaServerEnvironment.getEnv();
        if (StringUtils.isEmpty(env.getServerIp())) {
            return;
        }

        serverInfoMapper.delete(buildWrapper(env.getServerIp(), env.getServerPort()));
    }

    private static final int EXPIRED_TS = 10 * 30 * 1000;

    /**
     * 超过10次仍然没有上报过心跳的服务器，将其从种子节点的服务发现中剔除
     */
    @Scheduled(fixedDelay = EXPIRED_TS)
    public void removeExpiredServers() {
        Date yesterday = DateUtils.addMilliseconds(new Date(), -1 * EXPIRED_TS);

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
