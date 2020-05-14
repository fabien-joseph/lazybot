package com.lazybot.microservices.webapp.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.google.gson.Gson;
import com.lazybot.microservices.commons.model.Login;
import com.lazybot.microservices.commons.model.Position;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
public class WebappSocket {
    private static final Logger log = LoggerFactory.getLogger(WebappSocket.class);
    private SocketIOServer server;
    private Socket socketMaster;

    public WebappSocket(SocketIOServer serverWebapp) throws URISyntaxException {
        this.server = serverWebapp;
        this.socketMaster = IO.socket("http://localhost:9090");
        this.socketMaster.connect();
        server.addConnectListener(onConnected());
        server.addEventListener("sendMessage", String.class, this::sendMessage);
        server.addEventListener("goToPos", Position.class, this::goToPos);
        server.addEventListener("loadMap", Integer.class, this::loadMap);
        server.addEventListener("updateBot", String.class, this::updateBot);
        server.addEventListener("disconnectBot", Integer.class, this::disconnectBot);
        server.addEventListener("connectBot", Login.class, this::connectBot);
    }

    private void connectBot(SocketIOClient socketIOClient, Login login, AckRequest ackRequest) {
        System.out.println(login.getNickname() + " - " + login.getPassword());
        socketMaster.emit("connectBot", new Gson().toJson(login));
    }

    private void disconnectBot(SocketIOClient socketIOClient, Integer botId, AckRequest ackRequest) {
        socketMaster.emit("disconnectBot", botId);
    }

    private void updateBot(SocketIOClient socketIOClient, String jsonBot, AckRequest ackRequest) {
        System.out.println("updateBot");
        server.getBroadcastOperations().sendEvent("updateBot", jsonBot);
    }

    private void loadMap(SocketIOClient socketIOClient, Integer ray, AckRequest ackRequest) {
        socketMaster.emit("loadMap", ray);
    }

    private void goToPos(SocketIOClient socketIOClient, Position position, AckRequest ackRequest) {
        System.out.println(position);
        socketMaster.emit("goToPos", new Gson().toJson(position));
    }

    private ConnectListener onConnected() {
        System.out.println("Un client vient de se connecter :o");
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.debug("Client[{}] - Connected to chat module through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
        };
    }

    public void sendMessage(SocketIOClient client, String message, AckRequest ackSender) {
        socketMaster.emit("sendMessage", message);
        System.out.println("Rayon = " + message);
    }
}
