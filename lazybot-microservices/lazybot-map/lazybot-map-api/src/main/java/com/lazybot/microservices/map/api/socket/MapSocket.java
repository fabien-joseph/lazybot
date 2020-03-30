package com.lazybot.microservices.map.api.socket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.lazybot.microservices.map.business.CellManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MapSocket {
    private final CellManager cellManager;
    private Map<String, SocketIOClient> bots;

    public MapSocket(SocketIOServer server, CellManager cellManager) {
        bots = new HashMap<>();
        this.cellManager = cellManager;
        server.addEventListener("disconnectBot", String.class, this::disconnectBot);
        server.addEventListener("connectBot", String.class, this::connectBot);
        server.addEventListener("loadChunk", List.class, this::myMethod);
        server.addEventListener("getChunk", Integer.class, this::getChunk);
        server.addEventListener("callClient", String.class, this::callClient);
    }

    private void connectBot(SocketIOClient socketIOClient, String id, AckRequest ackRequest) throws InterruptedException {
        System.out.println("CONNEXION de " + id );
        socketIOClient.sendEvent("test");
        Thread.sleep(2000);
        bots.put(id, socketIOClient);
    }
    private void disconnectBot(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
        System.out.println("Déconnexion de " + id);
        bots.remove(id.toString());
    }


    public void callClient(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
        SocketIOClient client = bots.get(id);
        if (client != null) {
            client.sendEvent("test");
        } else {
            System.out.println("Client introuvable");
        }
    }

    public void myMethod (SocketIOClient client, List<Integer> data, AckRequest ackSender) throws Exception {
        System.out.println(data.size());
    }

    public void getChunk(SocketIOClient client, Integer ray, AckRequest ackSender) throws Exception {
        System.out.println("Test reçu sur lazybot-map ! Rayon = " + ray);
    }
}
