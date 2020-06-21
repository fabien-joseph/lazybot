package com.lazybot.microservices.master.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazybot.microservices.commons.manager.ToolsBotManager;
import com.lazybot.microservices.commons.model.*;
import com.lazybot.microservices.commons.model.mission.ExchangeMission;
import com.lazybot.microservices.master.model.BotIdentifier;
import io.socket.client.IO;
import io.socket.client.Socket;
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

    public MasterSocket(SocketIOServer serverMaster) throws URISyntaxException {
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
        this.server.addEventListener("loadMap", Integer.class, this::loadMap);
        this.server.addEventListener("exchange", String.class, this::exchange);
        this.server.addEventListener("getUpdateBot", String.class, this::getUpdateBot);
        this.server.addEventListener("getAllBotConnected", String.class, this::getAllBotConnected);
        this.server.addEventListener("getMissionCounts", String.class, this::getMissionCounts);
        this.server.addEventListener("executeMission", String.class, this::executeMission);
        this.server.addEventListener("connectBot", String.class, this::connectBot);
        this.server.addEventListener("disconnectBot", String.class, this::disconnectBot);

        // FROM MS MISSION
        this.server.addEventListener("look", String.class, this::look);
        this.server.addEventListener("drop", String.class, this::drop);
        this.server.addEventListener("missionStatus", String.class, this::missionStatus);
        this.server.addEventListener("updateTotalMissionDone", String.class, this::updateTotalMissionDone);
        this.server.addEventListener("updateTotalMissionFail", String.class, this::updateTotalMissionFail);
        this.server.addEventListener("updateTotalMissionRunning", String.class, this::updateTotalMissionRunning);

        // FROM BOTS
        this.server.addEventListener("registerBot", String.class, this::registerBot);
        this.server.addEventListener("unregisterBot", String.class, this::unregisterBot);
        this.server.addEventListener("returnLoadMap", List.class, this::returnLoadMap);
        this.server.addEventListener("updateBot", String.class, this::updateBot);
        this.server.addEventListener("missionDone", String.class, this::missionDone);
        this.server.addEventListener("missionFail", String.class, this::missionFail);
    }

    // CONNECTIONS MS - From all

    /**
     * Add the socket client in the list of client connected
     * @param socketIOClient Socket client
     * @param id ID unused
     * @param ackRequest request informations
     */
    private void connectMap(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
        this.socketMap = socketIOClient;
    }

    /**
     * Just an event to test the connection with other MS (for dev)
     * @param socketIOClient Socket client
     * @param t String to test
     * @param ackRequest request informations
     */
    private void test(SocketIOClient socketIOClient, String t, AckRequest ackRequest) {
        System.out.println("Retour de test reçu");
    }

    /**
     * Not implemented !
     *
     * An event to signal the error of the MS and the bot
     * @param socketIOClient Socket client
     * @param error Error message
     * @param ackRequest request informations
     */
    private void error(SocketIOClient socketIOClient, String error, AckRequest ackRequest) {
        System.out.println("Error: " + error);
    }

    // ORDERS / REQUEST INFOS FROM BOTS : WEBAPP - MISSION

    /**
     * An old event now unused to use the chat.
     * @param socketIOClient Socket client
     * @param id nothing
     * @param ackRequest request informations
     */
    private void chat(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
    }

    /**
     * Transmit a message that will be sent by a robot
     * @param socketIOClient Socket client
     * @param orderMessageJson message in JSON
     * @param ackRequest request informations
     */
    private void sendMessage(SocketIOClient socketIOClient, String orderMessageJson, AckRequest ackRequest) {
        broadcastOperation("sendMessage", orderMessageJson, String.class);
    }

    /**
     * Order bots to go to a position.
     * @param socketIOClient Socket client
     * @param orderPositionJson Position where bots will have to go
     * @param ackRequest request informations
     */
    private void goToPos(SocketIOClient socketIOClient, String orderPositionJson, AckRequest ackRequest) {
        broadcastOperation("goToPos", orderPositionJson, Position.class);
    }

    /**
     Unused since the Map MS has been abandoned.
     * Get the map aroung a bot
     * @param socketIOClient Socket client
     * @param ray Ray of the map to load
     * @param ackRequest request informations
     */
    private void loadMap(SocketIOClient socketIOClient, Integer ray, AckRequest ackRequest) {
        System.out.println("Rayon = " + ray);
        server.getBroadcastOperations().sendEvent("getLoadMap", ray);
    }

    /**
     * Send to the MS mission the exchange mission to treat it.
     * @param socketIOClient Socket client
     * @param jsonExchange Exchange object in JSON
     * @param ackRequest request informations
     */
    private void exchange(SocketIOClient socketIOClient, String jsonExchange, AckRequest ackRequest) {
        ExchangeMission exchange = new Gson().fromJson(jsonExchange, ExchangeMission.class);
        exchange.setBot1(bots.get(exchange.getBot1Username()).getBot());
        exchange.setBot2(bots.get(exchange.getBot2Username()).getBot());
        System.out.println(exchange);
        socketMission.emit("exchange", new Gson().toJson(exchange));
    }

    /**
     * Get the information of a bot.
     * @param socketIOClient Socket client
     * @param botUsername the username of the bot to get informations
     * @param ackRequest request informations
     */
    private void getUpdateBot(SocketIOClient socketIOClient, String botUsername, AckRequest ackRequest) {
        if (toolsBotManager.isBeginningWithWrongChar(botUsername))
            botUsername = toolsBotManager.correctBotUsername(botUsername);
        bots.get(botUsername).getSocketIOClient().sendEvent("getUpdateBot");
    }

    /**
     * Return to the webapp all the username of the bots actually connected.
     * @param socketIOClient Socket client
     * @param t an unused parameter
     * @param ackRequest request informations
     */
    private void getAllBotConnected(SocketIOClient socketIOClient, String t, AckRequest ackRequest) {
        socketWebapp.emit("allBotConnected", new Gson().toJson(bots.keySet()));
    }

    /**
     * Ask to the MS Mission the number of mission running
     * @param socketIOClient Socket client
     * @param t unused variable
     * @param ackRequest request informations
     */
    private void getMissionCounts(SocketIOClient socketIOClient, String t, AckRequest ackRequest) {
        socketMission.emit("getMissionCounts", new Gson().toJson(bots.keySet()));
    }

    /**
     * Infom the MS Mission that a mission has to be done.
     * @param socketIOClient Socket client
     * @param orderMission the order done
     * @param ackRequest request informations
     */
    private void executeMission(SocketIOClient socketIOClient, String orderMission, AckRequest ackRequest) {
        socketMission.emit("mission", orderMission);
    }

    /**
     * Execute command to connect a new Mineflayer robot.
     * @param socketIOClient Socket client
     * @param loginJson Login JSON
     * @param ackRequest request informations
     */
    private void connectBot(SocketIOClient socketIOClient, String loginJson, AckRequest ackRequest) throws IOException {
        Login loginBot = new Gson().fromJson(loginJson, Login.class);
        String command = "node C:/Users/Fabien/IdeaProjects/lazybot/lazybot-robot/robot.js";
        command += " --username=" + loginBot.getNickname();
        command += " --password=" + loginBot.getPassword();
        command += " --server=" + loginBot.getServer();
        Runtime.getRuntime().exec(command);
    }
    /**
     * Disconnect bots
     * @param socketIOClient Socket client
     * @param orderJson Order JSON to disconnect a bot
     * @param ackRequest request informations
     */
    private void disconnectBot(SocketIOClient socketIOClient, String orderJson, AckRequest ackRequest) {
        OrderBot<String> orderBot = new Gson().fromJson(orderJson, OrderBot.class);
        broadcastOperation("exit", orderJson, String.class);
    }

    // FROM MS MISSION

    /**
     * Ask a robot to look somewhere
     * @param socketIOClient Socket client
     * @param orderLookJson position to look JSON
     * @param ackRequest request informations
     */
    private void look(SocketIOClient socketIOClient, String orderLookJson, AckRequest ackRequest) {
        broadcastOperation("look", orderLookJson, Position.class);
    }

    /**
     * Ask robot to drop an item (ONLY ONE ACTUALLY, otherwise the robot would crash)
     * @param socketIOClient Socket client
     * @param orderDropJson order JSON to drop item
     * @param ackRequest request informations
     */
    private void drop(SocketIOClient socketIOClient, String orderDropJson, AckRequest ackRequest) {
        broadcastOperation("drop", orderDropJson, List.class);
    }

    /**
     * Give to a robot his mission status (the actual ID of the mission he is running)
     * @param socketIOClient Socket client
     * @param orderJson mission status
     * @param ackRequest request informations
     */
    private void missionStatus(SocketIOClient socketIOClient, String orderJson, AckRequest ackRequest) {
        broadcastOperation("missionStatus", orderJson, Integer.class);
    }

    /**
     * Update the number of missions done (to show in the webapp)
     * @param socketIOClient Socket client
     * @param countJson Count of the missions done
     * @param ackRequest request informations
     */
    private void updateTotalMissionDone(SocketIOClient socketIOClient, String countJson, AckRequest ackRequest) {
        System.out.println("Mission done " + countJson);
        socketWebapp.emit("updateTotalMissionDone", countJson);
    }

    /**
     * Update the number of missions fail (to show in the webapp)
     * @param socketIOClient Socket client
     * @param countJson Count of the missions done
     * @param ackRequest request informations
     */
    private void updateTotalMissionFail(SocketIOClient socketIOClient, String countJson, AckRequest ackRequest) {
        System.out.println("Mission fail " + countJson);
        socketWebapp.emit("updateTotalMissionFail", countJson);
    }

    /**
     * Update the number of missions running (to show in the webapp)
     * @param socketIOClient Socket client
     * @param countJson Count of the missions running
     * @param ackRequest request informations
     */
    private void updateTotalMissionRunning(SocketIOClient socketIOClient, String countJson, AckRequest ackRequest) {
        System.out.println("Mission running " + countJson);
        socketWebapp.emit("updateTotalMissionRunning", countJson);
    }

    // ==== FROM BOT ===

    /**
     * A robot is connecting to this MS Master. It has to be add to {@link MasterSocket#bots}, which is the list of the robot actually connected
     * @param socketIOClient Socket client
     * @param botUsername username of the bot to register
     * @param ackRequest request informations
     */
    private void registerBot(SocketIOClient socketIOClient, String botUsername, AckRequest ackRequest) {
        BotIdentifier botIdentifier = new BotIdentifier();
        botIdentifier.setSocketIOClient(socketIOClient);
        bots.put(botUsername, botIdentifier);
        socketIOClient.joinRoom("bots");
        socketWebapp.emit("allBotConnected", new Gson().toJson(bots.keySet()));
        socketIOClient.sendEvent("connectionSuccess");
        System.out.println("Nouveau bot connecté, id: " + botUsername + ". Total : " + bots.size());
    }

    /**
     * A robot is disconnecting to this MS Master. It has to be removed to {@link MasterSocket#bots}, which is the list of the robot actually connected
     * @param socketIOClient Socket client
     * @param botUsername username of the bot to unregister
     * @param ackRequest request informations
     */
    private void unregisterBot(SocketIOClient socketIOClient, String botUsername, AckRequest ackRequest) {
        bots.remove(botUsername);
        socketWebapp.emit("allBotConnected", new Gson().toJson(bots.keySet()));
        System.out.println("Un bot a été déconnecté. Total : " + bots.size());
    }

    /**
     * Unused since the Map MS has been abandoned.
     * Show the map represented by a list of integer
     * @param socketIOClient Socket client
     * @param map Map represented by a list of integer
     * @param ackRequest request informations
     */
    private void returnLoadMap(SocketIOClient socketIOClient, List<Integer> map, AckRequest ackRequest) {
        System.out.println("Size = " + map.size());
    }

    /**
     * Update the bot object in {@link MasterSocket#bots}
     * @param socketIOClient Socket client
     * @param jsonBot {@link Bot} in JSON
     * @param ackRequest request informations
     */
    private void updateBot(SocketIOClient socketIOClient, String jsonBot, AckRequest ackRequest) {
        Bot bot = new Gson().fromJson(jsonBot, Bot.class);
        bots.get(bot.getUsername()).setBot(bot);
        socketWebapp.emit("updateBotTest", jsonBot);
    }

    /**
     * Inform the MS Mission that a mission has been done successfully
     * @param socketIOClient Socket client
     * @param jsonMissionId id of the mission
     * @param ackRequest request informations
     */
    private void missionDone(SocketIOClient socketIOClient, String jsonMissionId, AckRequest ackRequest) {
        Integer missionId = new Gson().fromJson(jsonMissionId, Integer.class);
        socketMission.emit("missionDone", missionId);

    }

    /**
     * Inform the MS Mission that a mission failed
     * @param socketIOClient Socket client
     * @param jsonMissionId id of the mission
     * @param ackRequest request informations
     */
    private void missionFail(SocketIOClient socketIOClient, String jsonMissionId, AckRequest ackRequest) {
        Integer missionId = new Gson().fromJson(jsonMissionId, Integer.class);
        socketMission.emit("missionFail", missionId);
    }

    /**
     * Send an event robots.
     * @param event Name of the event to emit to the bot
     * @param orderJson {@link OrderBot} JSON
     * @param classOfData The type of data sent
     */
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

    /**
     * Generate a random ID between 100.000 and 999.999
     */
    private int generateId() {
        return ThreadLocalRandom.current().nextInt(100000, 999999 + 1);

    }
}
