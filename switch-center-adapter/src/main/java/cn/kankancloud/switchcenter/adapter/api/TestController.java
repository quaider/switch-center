package cn.kankancloud.switchcenter.adapter.api;

import akka.actor.Address;
import akka.cluster.Cluster;
import akka.cluster.Member;
import cn.kankancloud.switchcenter.adapter.application.akka.AkkaServerEnvironment;
import cn.kankancloud.switchcenter.adapter.driving.cache.SwitchCacheKey;
import cn.kankancloud.switchcenter.adapter.driving.cache.SwitchCacheManager;
import cn.kankancloud.switchcenter.adapter.driving.repository.switchinfo.dao.SwitchInfoPo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/akka")
@Slf4j
public class TestController {

    @Resource
    private SwitchCacheManager cacheManager;

    @GetMapping("/members")
    public List<NodeInfo> getNodes() {
        List<NodeInfo> ret = new ArrayList<>();
        final Cluster cluster = AkkaServerEnvironment.getEnv().getCluster();
        final Iterable<Member> members = cluster.state().getMembers();
        final Address leader = cluster.state().getLeader();
        members.forEach(f -> {
            NodeInfo info = new NodeInfo();
            info.setAddress(f.address());
            info.setLeaderAddr(leader);
            ret.add(info);
        });

        return ret;
    }

    @GetMapping("/cache")
    public Map<SwitchCacheKey, String> getCaches() {
        Map<SwitchCacheKey, String> ret = new HashMap<>();
        cacheManager.readView().forEach((k, v) -> {
            ret.put(k, v.map(SwitchInfoPo::getKey).orElse("empty"));
        });

        return ret;
    }

    @Data
    static class NodeInfo {
        private Address address;
        private Address leaderAddr;
    }

}
