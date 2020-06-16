package com.lazybot.microservices.master.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazybot.microservices.commons.manager.ToolsBotManager;
import com.lazybot.microservices.commons.model.*;
import com.lazybot.microservices.commons.model.mission.ExchangeMission;
import com.lazybot.microservices.commons.model.mission.Mission;
import com.lazybot.microservices.master.business.ConnectManager;
import com.lazybot.microservices.master.model.BotIdentifier;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MasterSocket {
    private Map<String, BotIdentifier> bots;
    private SocketIOServer server;
    private Socket socketWebapp;
    private SocketIOClient socketMap;
    private Socket socketMission;
    private ToolsBotManager toolsBotManager;
    private final ConnectManager connectManager;

    public MasterSocket(SocketIOServer serverMaster, ConnectManager connectManager) throws URISyntaxException {
        this.socketWebapp = IO.socket("http://localhost:8090");
        this.socketWebapp.connect();
        this.socketMission = IO.socket("http://localhost:9091");
        this.socketMission.connect();
        this.server = serverMaster;
        this.toolsBotManager = new ToolsBotManager();
        bots = new HashMap<>();

        // CONNECTIONS MS - From all
        this.server.addEventListener("connectMap", String.class, this::connectMap);
        this.server.addEventListener("test", String.class, this::test);
        this.server.addEventListener("error", String.class, this::error);

        // ORDERS / REQUEST INFOS FROM BOTS : WEBAPP - MISSION
        this.server.addEventListener("chat", String.class, this::chat);
        this.server.addEventListener("sendMessage", String.class, this::sendMessage);
        this.server.addEventListener("goToPos", String.class, this::goToPos);
        this.server.addEventListener("look", String.class, this::look);
        this.server.addEventListener("drop", String.class, this::drop);
        this.server.addEventListener("loadMap", Integer.class, this::loadMap);
        this.server.addEventListener("exchange", String.class, this::exchange);
        this.server.addEventListener("missionStatus", String.class, this::missionStatus);
        this.server.addEventListener("getUpdateBot", String.class, this::getUpdateBot);
        this.server.addEventListener("getAllBotConnected", String.class, this::getAllBotConnected);
        this.server.addEventListener("executeMission", String.class, this::executeMission);
        this.server.addEventListener("connectBot", String.class, this::connectBot);
        this.server.addEventListener("disconnectBot", String.class, this::disconnectBot);

        // FROM BOTS
        this.server.addEventListener("registerBot", String.class, this::registerBot);
        this.server.addEventListener("unregisterBot", String.class, this::unregisterBot);
        this.server.addEventListener("returnLoadMap", List.class, this::returnLoadMap);
        this.server.addEventListener("updateBot", String.class, this::updateBot);
        this.server.addEventListener("missionDone", String.class, this::missionDone);
        this.server.addEventListener("missionFail", String.class, this::missionFail);
        this.connectManager = connectManager;
    }

    private void error(SocketIOClient socketIOClient, String error, AckRequest ackRequest) {
        System.out.println("Error: " + error);
    }

    private void test(SocketIOClient socketIOClient, String t, AckRequest ackRequest) {
        System.out.println("Retour de test reçu");
    }

    private void getUpdateBot(SocketIOClient socketIOClient, String botUsername, AckRequest ackRequest) {
        if (toolsBotManager.isBeginningWithWrongChar(botUsername))
            botUsername = toolsBotManager.correctBotUsername(botUsername);
        bots.get(botUsername).getSocketIOClient().sendEvent("getUpdateBot");
    }

    private void getAllBotConnected(SocketIOClient socketIOClient, String t, AckRequest ackRequest) {
        socketWebapp.emit("allBotConnected", new Gson().toJson(bots.keySet()));
    }

    private void disconnectBot(SocketIOClient socketIOClient, String orderJson, AckRequest ackRequest) {
        OrderBot<String> orderBot = new Gson().fromJson(orderJson, OrderBot.class);
        broadcastOperation("exit", orderJson, String.class);
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
        Bot bot = new Gson().fromJson(jsonBot, Bot.class);
        bots.get(bot.getUsername()).setBot(bot);
        socketWebapp.emit("updateBotTest", jsonBot);
    }

    private void missionDone(SocketIOClient socketIOClient, String jsonMissionId, AckRequest ackRequest) {
        Integer missionId = new Gson().fromJson(jsonMissionId, Integer.class);
        socketMission.emit("missionDone", missionId);

    }

    private void missionFail(SocketIOClient socketIOClient, String jsonMissionId, AckRequest ackRequest) {
        Integer missionId = new Gson().fromJson(jsonMissionId, Integer.class);
        socketMission.emit("missionFail", missionId);
    }

    private void unregisterBot(SocketIOClient socketIOClient, String botUsername, AckRequest ackRequest) {
        bots.remove(botUsername);
        socketWebapp.emit("allBotConnected", new Gson().toJson(bots.keySet()));
        System.out.println("Un bot a été déconnecté. Total : " + bots.size());
    }

    private void missionStatus(SocketIOClient socketIOClient, String orderJson, AckRequest ackRequest) {
        broadcastOperation("missionStatus", orderJson, Integer.class);
    }

    private void exchange(SocketIOClient socketIOClient, String jsonExchange, AckRequest ackRequest) {
        ExchangeMission exchange = new Gson().fromJson(jsonExchange, ExchangeMission.class);
        exchange.setBot1(bots.get(exchange.getBot1Username()).getBot());
        exchange.setBot2(bots.get(exchange.getBot2Username()).getBot());
        System.out.println(exchange);
        // === Just for tests
/*        ExchangeMission exchange = new ExchangeMission();
        exchange.setId(generateId());
        exchange.setStep(0);
        exchange.setBot1(bots.get("Ronflonflon").getBot());
        exchange.setBot2(bots.get("test").getBot());
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 64, 0, "stone", "stone", 64, 36));
        exchange.setItemsGiveByBot1(items);
        exchange.setItemsGiveByBot2(null);*/
        // === Just for test ===
        socketMission.emit("exchange", new Gson().toJson(exchange));
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

    private void look(SocketIOClient socketIOClient, String orderLookJson, AckRequest ackRequest) {
        broadcastOperation("look", orderLookJson, Position.class);
    }

    private void drop(SocketIOClient socketIOClient, String orderDropJson, AckRequest ackRequest) {
        broadcastOperation("drop", orderDropJson, List.class);
    }


    private void sendMessage(SocketIOClient socketIOClient, String orderMessageJson, AckRequest ackRequest) {
        broadcastOperation("sendMessage", orderMessageJson, String.class);
    }

    private void chat(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
    }

    private void registerBot(SocketIOClient socketIOClient, String botUsername, AckRequest ackRequest) {
        BotIdentifier botIdentifier = new BotIdentifier();
        botIdentifier.setSocketIOClient(socketIOClient);
        bots.put(botUsername, botIdentifier);
        socketIOClient.joinRoom("bots");
        socketWebapp.emit("allBotConnected", new Gson().toJson(bots.keySet()));
        socketIOClient.sendEvent("connectionSuccess");
        System.out.println("Nouveau bot connecté, id: " + botUsername + ". Total : " + bots.size());
    }

    private void executeMission(SocketIOClient socketIOClient, String orderMission, AckRequest ackRequest) {
        socketMission.emit("mission", orderMission);
    }

    private void connectMap(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
        this.socketMap = socketIOClient;
    }

    private <T> void broadcastOperation(String event, String orderJson, Class<T> classOfData) {
        Type typePosition = new TypeToken<OrderBot<T>>() {
        }.getType();
        OrderBot<T> orderBot = new Gson().fromJson(orderJson, typePosition);
        if (toolsBotManager.isBeginningWithWrongChar(orderBot.getBotUsername()))
            orderBot.setBotUsername(toolsBotManager.correctBotUsername(orderBot.getBotUsername()));
        System.out.println(orderJson);
        System.out.println("Message : " + orderBot);
        List<SocketIOClient> clients = new ArrayList<>();
        if (orderBot.getBotUsername().equals("*")) {
            for (BotIdentifier b : bots.values()) {
                clients.add(b.getSocketIOClient());
            }
        } else {
            clients.add(bots.get(orderBot.getBotUsername()).getSocketIOClient());
        }

        for (SocketIOClient client : clients) {
            client.sendEvent(event, new Gson().toJson(orderBot.getData()));
        }
    }

    private int generateId() {
        return ThreadLocalRandom.current().nextInt(100000, 999999 + 1);

    }
}
