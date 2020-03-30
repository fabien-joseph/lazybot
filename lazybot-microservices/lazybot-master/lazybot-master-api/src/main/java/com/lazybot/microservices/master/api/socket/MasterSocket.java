package com.lazybot.microservices.master.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.stereotype.Service;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MasterSocket {
    private Socket clientMission;
    private Socket clientMap;
    private Map<String, SocketIOClient> bots;
    public MasterSocket(SocketIOServer server) throws URISyntaxException {
        bots = new HashMap<>();
        this.clientMission = IO.socket("http://localhost:9091");
        this.clientMap = IO.socket("http://localhost:9092");
        this.clientMission.connect();
        this.clientMap.connect();

        server.addEventListener("disconnectBot", String.class, this::disconnectBot);
        server.addEventListener("connectBot", String.class, this::connectBot);
        server.addEventListener("test", String.class, this::testPassClient);
        server.addEventListener("getChunk", Integer.class, this::getChunk);
    }

    private void connectBot(SocketIOClient socketIOClient, String id, AckRequest ackRequest) throws InterruptedException {
        System.out.println("CONNEXION de " + id );
        bots.put(id, socketIOClient);
    }
    private void disconnectBot(SocketIOClient socketIOClient, String id, AckRequest ackRequest) {
        System.out.println("Déconnexion de " + id);
        bots.remove(id.toString());
    }

    private void testPassClient(SocketIOClient socketIOClient, String test, AckRequest ackRequest)
    {
        System.out.println("TEST DE PASSAGE");
        //clientMap.emit("callClient", socketIOClient);
    }

    public void getChunk(SocketIOClient client, Integer ray, AckRequest ackSender) throws Exception {
        clientMap.emit("getChunk", ray);
        System.out.println("Test reçu sur lazybot-master !");
    }
}
