package com.lazybot.microservices.webapp.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.google.gson.Gson;
import com.lazybot.microservices.commons.model.Bot;
import com.lazybot.microservices.commons.model.Position;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
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
        server.addEventListener("healthChange", String.class, this::healthChange);
        server.addEventListener("loadMap", Integer.class, this::loadMap);
    }

    private void healthChange(SocketIOClient socketIOClient, String jsonBot, AckRequest ackRequest) {
        Bot bot = new Gson().fromJson(jsonBot, Bot.class);
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
