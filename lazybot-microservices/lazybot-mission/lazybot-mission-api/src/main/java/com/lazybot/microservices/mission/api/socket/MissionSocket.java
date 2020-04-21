package com.lazybot.microservices.mission.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.lazybot.microservices.mission.model.Bot;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
public class MissionSocket {
    private Socket socketMaster;

    public MissionSocket(SocketIOServer server) throws URISyntaxException {
        this.socketMaster = IO.socket("http://localhost:9090");
        this.socketMaster.connect();
        registrateToMaster();
        server.addEventListener("connectBot", Bot.class, this::connectBot);
        server.addEventListener("getChunk", Integer.class, this::getChunk);
    }

    public void connectBot(SocketIOClient client, Bot bot, AckRequest ackSender) throws Exception {
        bot.start();
        Thread.sleep(5000);
        bot.stop();
    }

    public void getChunk(SocketIOClient client, Integer ray, AckRequest ackSender) throws Exception {
        System.out.println("Test reçu sur lazybot-mission !");
    }

    private void registrateToMaster() {
        socketMaster.emit("connectMission");
    }
}
