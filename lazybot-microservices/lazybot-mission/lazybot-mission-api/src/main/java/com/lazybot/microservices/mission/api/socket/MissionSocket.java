package com.lazybot.microservices.mission.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.lazybot.microservices.mission.model.Bot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissionSocket {

    public MissionSocket(SocketIOServer server) {
        server.addEventListener("connectBot", Bot.class, this::connectBot);
    }

    public void connectBot(SocketIOClient client, Bot bot, AckRequest ackSender) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("node",
                "C:\\Users\\Fabien\\IdeaProjects\\lazybot\\lazybot-robot\\robot.js",
                "--username=" + bot.getUsername(),
                "--password=" + bot.getPassword());
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();
        Thread.sleep(5000);
        p.destroy();
    }
}
