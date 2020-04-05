package com.lazybot.microservices.master.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.google.gson.Gson;
import com.lazybot.microservices.master.business.ConnectManager;
import com.lazybot.microservices.master.model.Position;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.stereotype.Service;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MasterSocket {
    private Map<String, SocketIOClient> bots;
    private SocketIOServer server;
    private Socket socketWebapp;

    private final ConnectManager connectManager;

    public MasterSocket(SocketIOServer serverMaster, ConnectManager connectManager) throws URISyntaxException {
        this.socketWebapp = IO.socket("http://localhost:8090");
        this.socketWebapp.connect();
        this.server = serverMaster;
        bots = new HashMap<>();
        this.server.addEventListener("chat", String.class, this::chat);
        this.server.addEventListener("connectBot", String.class, this::connectBot);
        this.server.addEventListener("sendMessage", String.class, this::sendMessage);
        this.server.addEventListener("goToPos", String.class, this::goToPos);
        this.server.addEventListener("healthChange", Integer.class, this::healthChange);
        this.connectManager = connectManager;
    }

    private void healthChange(SocketIOClient socketIOClient, Integer health, AckRequest ackRequest) {
        socketWebapp.emit("healthChange", health);
    }

    private void goToPos(SocketIOClient socketIOClient, String pos, AckRequest ackRequest) throws MismatchedInputException {
        Position position = new Gson().fromJson(pos, Position.class);
        System.out.println("x = " + position.getX() + ", y = " + position.getY() + ", z = " + position.getZ());
    }

    private void sendMessage(SocketIOClient socketIOClient, String message, AckRequest ackRequest) {
        System.out.println("SessionId : " + socketIOClient.getSessionId() + ", Message : " + message);
        server.getBroadcastOperations().sendEvent("sendMessage", message);
    }

    private void connectBot(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
        System.out.println("Ajout d'un bot...");
        bots.put(id, socketIOClient);
        server.getBroadcastOperations().sendEvent("test", "Salut", "Resalut");
        System.out.println("Le bot id " + id + " a été ajouté. Nombre de bots totaux : " + bots.size());
    }

    private void chat(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {

    }
}
