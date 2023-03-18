package cn.kankancloud.switchcenter.adapter.application.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.cluster.Cluster;
import cn.kankancloud.switchcenter.adapter.util.NetUtils;
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

    @Setter(AccessLevel.PACKAGE)
    private Cluster cluster;

    @Setter(AccessLevel.PACKAGE)
    private ActorRef publisher;

    public static final String AKKA_ACTOR_SYSTEM_PATH = "akka://%s@%s:%s";
    public static final String AKKA_NODE_PATH = "akka://%s@%s/user/%s";
    public static final String ACTOR_SYS_NAME = "switch-center";


    public String getAkkaSystemPath() {
        return String.format(AKKA_ACTOR_SYSTEM_PATH, ACTOR_SYS_NAME, serverIp, serverPort);
    }

    public static String buildActorPath(String ip, int port, String targetActorName) {
        String address = ip + ":" + port;
        return String.format(AKKA_NODE_PATH, ACTOR_SYS_NAME, address, targetActorName);
    }

    public String getActorPath(String targetActorName) {
        String address = serverIp + ":" + serverPort;
        return String.format(AKKA_NODE_PATH, ACTOR_SYS_NAME, address, targetActorName);
    }

}
