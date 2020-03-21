package com.lazybot.microservices.mission.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.lazybot.microservices.mission.model.Bot;
import org.springframework.stereotype.Service;

@Service
public class MissionSocket {

    public MissionSocket(SocketIOServer server) {
        server.addEventListener("connectBot", Bot.class, this::connectBot);
    }

    public void connectBot(SocketIOClient client, Bot bot, AckRequest ackSender) throws Exception {
        bot.start();
        Thread.sleep(5000);
        bot.stop();
    }
}
