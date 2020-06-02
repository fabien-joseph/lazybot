package com.lazybot.microservices.master.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazybot.microservices.commons.manager.ToolsBotManager;
import com.lazybot.microservices.commons.model.Login;
import com.lazybot.microservices.commons.model.Order;
import com.lazybot.microservices.commons.model.Position;
import com.lazybot.microservices.master.business.ConnectManager;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class MasterSocket {
    private Map<String, SocketIOClient> bots;
    private SocketIOServer server;
    private Socket socketWebapp;
    private SocketIOClient socketMap;
    private SocketIOClient socketMission;
    private ToolsBotManager toolsBotManager;
    private final ConnectManager connectManager;

    public MasterSocket(SocketIOServer serverMaster, ConnectManager connectManager) throws URISyntaxException {
        this.socketWebapp = IO.socket("http://localhost:8090");
        this.socketWebapp.connect();
        this.server = serverMaster;
        this.toolsBotManager = new ToolsBotManager();
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
        this.server.addEventListener("getUpdateBot", String.class, this::getUpdateBot);
        this.server.addEventListener("getAllBotConnected", String.class, this::getAllBotConnected);
        this.server.addEventListener("connectBot", String.class, this::connectBot);
        this.server.addEventListener("disconnectBot", String.class, this::disconnectBot);

        // FROM BOTS
        this.server.addEventListener("registerBot", String.class, this::registerBot);
        this.server.addEventListener("unregisterBot", String.class, this::unregisterBot);
        this.server.addEventListener("returnLoadMap", List.class, this::returnLoadMap);
        this.server.addEventListener("updateBot", String.class, this::updateBot);
        this.connectManager = connectManager;
    }

    private void getUpdateBot(SocketIOClient socketIOClient, String botUsername, AckRequest ackRequest) {
        if (toolsBotManager.isBeginningWithWrongChar(botUsername))
            botUsername = toolsBotManager.correctBotUsername(botUsername);
        bots.get(botUsername).sendEvent("getUpdateBot");
    }

    private void getAllBotConnected(SocketIOClient socketIOClient, String t, AckRequest ackRequest) {
        socketWebapp.emit("allBotConnected", new Gson().toJson(bots.keySet()));
    }

    private void disconnectBot(SocketIOClient socketIOClient, String orderJson, AckRequest ackRequest) {
        //broadcastOperation("exit", orderJson, null);
        server.getRoomOperations("bots").sendEvent("exit");
    }

    private void connectBot(SocketIOClient socketIOClient, String login, AckRequest ackRequest) throws IOException {
        Login loginBot = new Gson().fromJson(login, Login.class);
        String command = "node C:/Users/Fabien/IdeaProjects/lazybot/lazybot-robot/robot.js";
        command += " --username=" + loginBot.getNickname();
        command += " --password=" + loginBot.getPassword();
        command += " --server=" + loginBot.getServer();
        Runtime.getRuntime().exec(command);
    }

    private void updateBot(SocketIOClient socketIOClient, String jsonBot, AckRequest ackRequest) {
        socketWebapp.emit("updateBotTest", jsonBot);
    }

    private void unregisterBot(SocketIOClient socketIOClient, String botUsername, AckRequest ackRequest) {
        bots.remove(botUsername);
        socketWebapp.emit("allBotConnected", new Gson().toJson(bots.keySet()));
        System.out.println("Un bot a été déconnecté. Total : " + bots.size());
    }

    private void exchange(SocketIOClient socketIOClient, String name, AckRequest ackRequest) {
    }

    private void returnLoadMap(SocketIOClient socketIOClient, List<Integer> map, AckRequest ackRequest) {
        System.out.println("Size = " + map.size());
    }

    private void loadMap(SocketIOClient socketIOClient, Integer ray, AckRequest ackRequest) {
        System.out.println("Rayon = " + ray);
        server.getBroadcastOperations().sendEvent("getLoadMap", ray);
    }

    private void goToPos(SocketIOClient socketIOClient, String orderPositionJson, AckRequest ackRequest) {
        broadcastOperation("goToPos", orderPositionJson, Position.class);
    }

    private void sendMessage(SocketIOClient socketIOClient, String orderMessageJson, AckRequest ackRequest) {
        broadcastOperation("sendMessage", orderMessageJson, String.class);
    }

    private void chat(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
    }

    private void registerBot(SocketIOClient socketIOClient, String botUsername, AckRequest ackRequest) {
        bots.put(botUsername, socketIOClient);
        socketIOClient.joinRoom("bots");
        socketWebapp.emit("allBotConnected", new Gson().toJson(bots.keySet()));
        socketIOClient.sendEvent("connectionSuccess");
        System.out.println("Nouveau bot connecté, id: " + botUsername + ". Total : " + bots.size());
    }

    private void connectMap(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
        this.socketMap = socketIOClient;
    }

    private void connectMission(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
        this.socketMission = socketIOClient;
    }

    private <T> void broadcastOperation(String event, String orderJson, Class<T> classOfData) {
        Type typePosition = new TypeToken<Order<T>>() {
        }.getType();
        Order<T> order = new Gson().fromJson(orderJson, typePosition);
        if (toolsBotManager.isBeginningWithWrongChar(order.getBotUsername()))
            order.setBotUsername(toolsBotManager.correctBotUsername(order.getBotUsername()));
        System.out.println("Message : " + order);
        List<SocketIOClient> clients;
        if (order.getBotUsername().equals("*")) {
            clients = new ArrayList<>(bots.values());
        } else {
            clients = new ArrayList<>();
            clients.add(bots.get(order.getBotUsername()));
        }

        for (SocketIOClient client : clients) {
            client.sendEvent(event, new Gson().toJson(order.getData()));
        }

    }
}
