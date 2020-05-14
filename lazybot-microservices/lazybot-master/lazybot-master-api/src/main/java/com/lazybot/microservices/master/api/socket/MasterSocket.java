package com.lazybot.microservices.master.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.google.gson.Gson;
import com.lazybot.microservices.commons.model.Bot;
import com.lazybot.microservices.commons.model.Login;
import com.lazybot.microservices.commons.model.Position;
import com.lazybot.microservices.master.business.ConnectManager;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MasterSocket {
    private Map<String, SocketIOClient> bots;
    private SocketIOServer server;
    private Socket socketWebapp;
    private SocketIOClient socketMap;
    private SocketIOClient socketMission;

    private final ConnectManager connectManager;

    public MasterSocket(SocketIOServer serverMaster, ConnectManager connectManager) throws URISyntaxException {
        this.socketWebapp = IO.socket("http://localhost:8090");
        this.socketWebapp.connect();
        this.server = serverMaster;
        bots = new HashMap<>();

        // CONNECTIONS MS
        this.server.addEventListener("connectMap", String.class, this::connectMap);
        this.server.addEventListener("connectMission", String.class, this::connectMission);

        // WEBAPP
        this.server.addEventListener("chat", String.class, this::chat);
        this.server.addEventListener("sendMessage", String.class, this::sendMessage);
        this.server.addEventListener("goToPos", String.class, this::goToPos);
        this.server.addEventListener("loadMap", Integer.class, this::loadMap);
        this.server.addEventListener("exchange", String.class, this::exchange);
        this.server.addEventListener("connectBot", String.class, this::connectBot);
        this.server.addEventListener("disconnectBot", Integer.class, this::disconnectBot);

        // FROM BOTS
        this.server.addEventListener("registerBot", String.class, this::registerBot);
        this.server.addEventListener("unregisterBot", String.class, this::unregisterBot);
        this.server.addEventListener("returnLoadMap", List.class, this::returnLoadMap);
        this.server.addEventListener("updateBot", String.class, this::updateBot);
        this.connectManager = connectManager;
    }

    private void disconnectBot(SocketIOClient socketIOClient, Integer bot, AckRequest ackRequest) {
        server.getRoomOperations("bots").sendEvent("exit");
    }

    private void connectBot(SocketIOClient socketIOClient, String login, AckRequest ackRequest) throws IOException {
        Login loginBot = new Gson().fromJson(login, Login.class);
        String command = "node C:/Users/Fabien/IdeaProjects/lazybot/lazybot-robot/robot.js";
        command += " --username=" + loginBot.getNickname();
        if (loginBot.getPassword() != null)
            command += " --password=" + loginBot.getPassword();
        Runtime.getRuntime().exec(command);
    }

    private void updateBot(SocketIOClient socketIOClient, String jsonbot, AckRequest ackRequest) {
        System.out.println("updateBot");
        Bot bot = new Gson().fromJson(jsonbot, Bot.class);
        socketWebapp.emit("updateBot", new Gson().toJson(bot));
    }

    private void unregisterBot(SocketIOClient socketIOClient, String botId, AckRequest ackRequest) {
        bots.remove(botId);
        System.out.println("Un bot a été déconnecté. Total : " + bots.size());
    }

    private void exchange(SocketIOClient socketIOClient, String name, AckRequest ackRequest) {
    }

    private void returnLoadMap(SocketIOClient socketIOClient, List<Integer> map, AckRequest ackRequest) {
        System.out.println("Size = " + map.size());
        //socketMap.sendEvent("loadMap", ray);
    }

    private void loadMap(SocketIOClient socketIOClient, Integer ray, AckRequest ackRequest) {
        System.out.println("Rayon = " + ray);
        server.getBroadcastOperations().sendEvent("getLoadMap", ray);
    }

    private void goToPos(SocketIOClient socketIOClient, String pos, AckRequest ackRequest) throws MismatchedInputException {
        Position position = new Gson().fromJson(pos, Position.class);
        System.out.println(position);
        server.getRoomOperations("bots").sendEvent("goToPos", position.getX(), position.getY(), position.getZ());
    }

    private void sendMessage(SocketIOClient socketIOClient, String message, AckRequest ackRequest) {
        server.getBroadcastOperations().sendEvent("sendMessage", message);
    }

    private void chat(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
    }

    private void registerBot(SocketIOClient socketIOClient, String botId, AckRequest ackRequest) {
        bots.put(botId, socketIOClient);
        socketIOClient.joinRoom("bots");
        socketIOClient.sendEvent("connectionSuccess");
        System.out.println("Nouveau bot connecté, id: " + botId + ". Total : " + bots.size());
    }

    private void connectMap(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
        this.socketMap = socketIOClient;
    }

    private void connectMission(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
        this.socketMission = socketIOClient;
    }
}
