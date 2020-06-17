package com.lazybot.microservices.webapp.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import com.lazybot.microservices.commons.manager.ToolsBotManager;
import com.lazybot.microservices.commons.model.Login;
import com.lazybot.microservices.commons.model.OrderBot;
import com.lazybot.microservices.commons.model.mission.ExchangeMission;
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
    private ToolsBotManager toolsBotManager;

    public WebappSocket(SocketIOServer serverWebapp) throws URISyntaxException {
        this.server = serverWebapp;
        this.socketMaster = IO.socket("http://localhost:9090");
        this.socketMaster.connect();
        this.toolsBotManager = new ToolsBotManager();

        // === FROM MASTER MS ===
        server.addEventListener("allBotConnected", String.class, this::allBotConnected);
        server.addEventListener("updateBotTest", String.class, this::updateBot);
        server.addEventListener("updateTotalMissionDone", String.class, this::updateTotalMissionDone);
        server.addEventListener("updateTotalMissionFail", String.class, this::updateTotalMissionFail);
        server.addEventListener("updateTotalMissionRunning", String.class, this::updateTotalMissionRunning);

        // === FROM WEBAPP ===
        server.addEventListener("connectBot", Login.class, this::connectBot);
        server.addEventListener("disconnectBot", String.class, this::disconnectBot);
        server.addEventListener("getAllBotConnected", String.class, this::getAllBotConnected);
        server.addEventListener("getMissionCounts", String.class, this::getMissionCounts);
        server.addEventListener("getUpdateBot", String.class, this::getUpdateBot);
        server.addEventListener("loadMap", Integer.class, this::loadMap);
        server.addEventListener("sendMessageTest", OrderBot.class, this::sendMessage);
        server.addEventListener("goToPos", String.class, this::goToPos);
        server.addEventListener("exchange", String.class, this::exchange);
    }

    private void exchange(SocketIOClient socketIOClient, String jsonExchange, AckRequest ackRequest) {
        System.out.println("EXCHANGE : " + jsonExchange);
        socketMaster.emit("exchange", jsonExchange);
    }

    /**
    From master MS, give all the names of the bots connected.
     @param socketIOClient socket client of master MS.
     @param botUsernames array of strings with all the names of the bots.
    */
    private void allBotConnected(SocketIOClient socketIOClient, String botUsernames, AckRequest ackRequest) {
        server.getBroadcastOperations().sendEvent("allBotConnected", botUsernames);
    }


    /**
     From master MS. Update the number of missions done
     @param socketIOClient socket client of master MS.
     @param countJson bot updated.
    */
    private void updateTotalMissionDone(SocketIOClient socketIOClient, String countJson, AckRequest ackRequest) {
        System.out.println("Mission done : " + countJson);
        server.getBroadcastOperations().sendEvent("updateTotalMissionDoneWebapp", countJson);
    }

    /**
    From master MS. Update the number of missions fail
     @param socketIOClient socket client of master MS.
     @param countJson bot updated.
    */
    private void updateTotalMissionFail(SocketIOClient socketIOClient, String countJson, AckRequest ackRequest) {
        System.out.println("Mission fail : " + countJson);
        server.getBroadcastOperations().sendEvent("updateTotalMissionFailWebapp", countJson);
    }

    /**
    From master MS. Update the number of missions running
     @param socketIOClient socket client of master MS.
     @param countJson bot updated.
    */
    private void updateTotalMissionRunning(SocketIOClient socketIOClient, String countJson, AckRequest ackRequest) {
        System.out.println("Mission running : " + countJson);
        server.getBroadcastOperations().sendEvent("updateTotalMissionRunningWebapp", countJson);
    }



    /**
    From master MS. Update a bot (life, food, inventory, etc...).
     @param socketIOClient socket client of master MS.
     @param jsonBot bot updated.
    */
    private void updateBot(SocketIOClient socketIOClient, String jsonBot, AckRequest ackRequest) {
        server.getBroadcastOperations().sendEvent("updateBot", jsonBot);
    }



    /**
    From webapp. Ask to master MS to execute a new bot
     @param socketIOClient socket client of master MS.
     @param login informations of login (username and password if premium)
     */
    private void connectBot(SocketIOClient socketIOClient, Login login, AckRequest ackRequest) {
        socketMaster.emit("connectBot", new Gson().toJson(login));
    }

    /**
   From webapp. Ask to master MS to stop all the bots
     @param socketIOClient socket client of master MS.
     @param botUsername name of the bot to disconnect.
     */
    private void disconnectBot(SocketIOClient socketIOClient, String botUsername, AckRequest ackRequest) {
        socketMaster.emit("disconnectBot", botUsername);
    }

    /**
    From webapp. Ask to master MS to get an update of a bot.
     @param socketIOClient socket client of master MS.
     @param botUsername username of the bot to update.
     */
    private void getUpdateBot(SocketIOClient socketIOClient, String botUsername, AckRequest ackRequest) {
        socketMaster.emit("getUpdateBot", botUsername);
    }

    /**
    From webapp. Ask to master MS all the nicknames of the bots connected
     @param socketIOClient socket client of master MS.
     @param t useless variable.
     */
    private void getAllBotConnected(SocketIOClient socketIOClient, String t, AckRequest ackRequest) {
        socketMaster.emit("getAllBotConnected");
    }

    /**
    From webapp. Ask to master MS all the nicknames of the bots connected
     @param socketIOClient socket client of master MS.
     @param t useless variable.
     */
    private void getMissionCounts(SocketIOClient socketIOClient, String t, AckRequest ackRequest) {
        socketMaster.emit("getMissionCounts");
    }

    /**
   From webapp. Ask to master to load a map around all the bots.
     @param socketIOClient socket client of master MS.
     @param ray ray of the blocs to load.
     */
    private void loadMap(SocketIOClient socketIOClient, Integer ray, AckRequest ackRequest) {
        socketMaster.emit("loadMap", ray);
    }

    /**
    From webapp. Ask to master MS to order all bots to send a message.
     @param socketIOClient socket client of master MS.
     @param orderBotMessage message to send.
     */
    public void sendMessage(SocketIOClient socketIOClient, OrderBot<String> orderBotMessage, AckRequest ackSender) {
        socketMaster.emit("sendMessage", new Gson().toJson(orderBotMessage));
    }

    /**
    From webapp. Ask to master MS to order all bots to go to a position.
     @param socketIOClient socket client of master MS.
     @param orderPositionJson position to go.
     @return
    */
    private void goToPos(SocketIOClient socketIOClient, String orderPositionJson, AckRequest ackRequest) {
        socketMaster.emit("goToPos", orderPositionJson);
    }
}
