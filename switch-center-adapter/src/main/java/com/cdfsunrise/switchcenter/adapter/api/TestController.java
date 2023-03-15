package com.cdfsunrise.switchcenter.adapter.api;

import akka.actor.Address;
import akka.cluster.Cluster;
import akka.cluster.Member;
import com.cdfsunrise.switchcenter.adapter.application.akka.AkkaServerEnvironment;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/akka")
@Slf4j
public class TestController {

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

    @Data
    static class NodeInfo {
        private Address address;
        private Address leaderAddr;
    }

}
