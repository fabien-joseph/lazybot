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
import java.util.ArrayList;
import java.util.List;

@Service
public class WebappSocket {
    private static final Logger log = LoggerFactory.getLogger(WebappSocket.class);
    private SocketIOServer server;
    private Socket socketMaster;

    public WebappSocket(SocketIOServer serverWebapp) throws URISyntaxException {
        this.server = serverWebapp;
        this.socketMaster = IO.socket("http://localhost:9090");
        this.socketMaster.connect();

        server.addEventListener("sendMessage", String.class, this::sendMessage);
        server.addEventListener("goToPos", Position.class, this::goToPos);
        server.addEventListener("loadMap", Integer.class, this::loadMap);
        server.addEventListener("updateBot", String.class, this::updateBot);
        server.addEventListener("disconnectBot", Integer.class, this::disconnectBot);
        server.addEventListener("connectBot", Login.class, this::connectBot);
        server.addEventListener("allBotConnected", String.class, this::registerBot);
        server.addEventListener("getAllBotConnected", String.class, this::getAllBotConnected);
    }

    private void getAllBotConnected(SocketIOClient socketIOClient, String t, AckRequest ackRequest) {
        socketMaster.emit("getAllBotConnected");
    }

    private void registerBot(SocketIOClient socketIOClient, String botUsernames, AckRequest ackRequest) {
        server.getBroadcastOperations().sendEvent("allBotConnected", botUsernames);
    }

    private void connectBot(SocketIOClient socketIOClient, Login login, AckRequest ackRequest) {
        System.out.println(login.getNickname() + " - " + login.getPassword());
        socketMaster.emit("connectBot", new Gson().toJson(login));
    }

    private void disconnectBot(SocketIOClient socketIOClient, Integer botId, AckRequest ackRequest) {
        socketMaster.emit("disconnectBot", botId);
    }

    private void updateBot(SocketIOClient socketIOClient, String jsonBot, AckRequest ackRequest) {
        //System.out.println("updateBot");
        server.getBroadcastOperations().sendEvent("updateBot", jsonBot);
    }

    private void loadMap(SocketIOClient socketIOClient, Integer ray, AckRequest ackRequest) {
        socketMaster.emit("loadMap", ray);
    }

    private void goToPos(SocketIOClient socketIOClient, Position position, AckRequest ackRequest) {
        System.out.println(position);
        socketMaster.emit("goToPos", new Gson().toJson(position));
    }

    public void sendMessage(SocketIOClient client, String message, AckRequest ackSender) {
        socketMaster.emit("sendMessage", message);
        System.out.println("Rayon = " + message);
    }
}
