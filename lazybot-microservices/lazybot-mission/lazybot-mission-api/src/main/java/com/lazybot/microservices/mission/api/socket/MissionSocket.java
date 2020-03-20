package com.lazybot.microservices.mission.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissionSocket {

    public MissionSocket(SocketIOServer server) {
        server.addEventListener("connectABot", String.class, this::connectABot);
    }

    public void connectABot(SocketIOClient client, String infoConnection, AckRequest ackSender) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("node", "C:\\Users\\Fabien\\IdeaProjects\\lazybot\\lazybot-robot\\robot.js", "--username=Bot");
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();
        Thread.sleep(5000);
        p.destroy();
    }
}
