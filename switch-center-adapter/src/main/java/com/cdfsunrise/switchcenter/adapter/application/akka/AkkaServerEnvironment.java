package com.cdfsunrise.switchcenter.adapter.application.akka;

import akka.actor.ActorSystem;
import com.cdfsunrise.switchcenter.adapter.util.NetUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class AkkaServerEnvironment {

    private static final AkkaServerEnvironment env = new AkkaServerEnvironment();

    private AkkaServerEnvironment() {
    }

    public static AkkaServerEnvironment getEnv() {
        return env;
    }

    public synchronized void init() {
        if (!StringUtils.isEmpty(serverIp)) {
            return;
        }

        serverIp = NetUtils.getLocalHost();
        serverPort = NetUtils.getRandomPort();
    }

    private String serverIp;
    private int serverPort;

    @Setter(AccessLevel.PACKAGE)
    private ActorSystem actorSystem;

    public static final String AKKA_NODE_PATH = "akka://%s@%s/user/%s";
    public static final String ACTOR_SYS_NAME = "switch-center";

    public String getActorPath(String ip, int port, String targetActorName) {
        String address = ip + ":" + port;
        return String.format(AKKA_NODE_PATH, ACTOR_SYS_NAME, address, targetActorName);
    }

}
