package com.lazybot.microservices.master.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.google.gson.Gson;
import com.lazybot.microservices.commons.model.Bot;
import com.lazybot.microservices.commons.model.Position;
import com.lazybot.microservices.master.business.ConnectManager;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.stereotype.Service;
import java.net.URISyntaxException;
import java.util.ArrayList;
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

        // CONNECTIONS
        this.server.addEventListener("connectMap", String.class, this::connectMap);
        this.server.addEventListener("connectMission", String.class, this::connectMission);

        // FROM WEBAPP
        this.server.addEventListener("chat", String.class, this::chat);
        this.server.addEventListener("connectBot", String.class, this::connectBot);
        this.server.addEventListener("sendMessage", String.class, this::sendMessage);
        this.server.addEventListener("goToPos", String.class, this::goToPos);
        this.server.addEventListener("healthChange", Bot.class, this::healthChange);
        this.server.addEventListener("loadMap", Integer.class, this::loadMap);

        // FROM BOTS
        this.server.addEventListener("returnLoadMap", List.class, this::returnLoadMap);

        this.connectManager = connectManager;
    }

    private void returnLoadMap(SocketIOClient socketIOClient, List<Integer> map, AckRequest ackRequest) {
        System.out.println("Size = " + map.size());
        //socketMap.sendEvent("loadMap", ray);
    }

    private void loadMap(SocketIOClient socketIOClient, Integer ray, AckRequest ackRequest) {
        System.out.println("Rayon = " + ray);
        server.getBroadcastOperations().sendEvent("getLoadMap", ray);
    }

    private void healthChange(SocketIOClient socketIOClient, Bot bot, AckRequest ackRequest) {
        socketWebapp.emit("healthChange", new Gson().toJson(bot));
    }

    private void goToPos(SocketIOClient socketIOClient, String pos, AckRequest ackRequest) throws MismatchedInputException {
        Position position = new Gson().fromJson(pos, Position.class);
        System.out.println("x = " + position.getX() + ", z = " + position.getZ());
    }

    private void sendMessage(SocketIOClient socketIOClient, String message, AckRequest ackRequest) {
        System.out.println("SessionId : " + socketIOClient.getSessionId() + ", Message : " + message);
        server.getBroadcastOperations().sendEvent("sendMessage", message);
    }

    private void chat(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
    }

    private void connectBot(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
        System.out.println("Ajout d'un bot...");
        bots.put(id, socketIOClient);
        server.getBroadcastOperations().sendEvent("test", "Salut", "Resalut");
        System.out.println("Le bot id " + id + " a été ajouté. Nombre de bots totaux : " + bots.size());
    }
    private void connectMap(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
        System.out.println("Socket MAP connecté");
        this.socketMap = socketIOClient;
    }
    private void connectMission(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
        System.out.println("Socket MISSION connecté");
        this.socketMission = socketIOClient;
    }

}
