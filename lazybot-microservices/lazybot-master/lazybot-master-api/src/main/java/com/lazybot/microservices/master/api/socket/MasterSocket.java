package com.lazybot.microservices.master.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.stereotype.Service;
import java.net.URISyntaxException;

@Service
public class MasterSocket {
    private Socket clientMission;
    private Socket clientMap;
    public MasterSocket(SocketIOServer server) throws URISyntaxException {
        this.clientMission = IO.socket("http://localhost:9091");
        this.clientMap = IO.socket("http://localhost:9092");
        this.clientMission.connect();
        this.clientMap.connect();
        server.addEventListener("getChunk", Integer.class, this::getChunk);
    }

    public void getChunk(SocketIOClient client, Integer ray, AckRequest ackSender) throws Exception {
        clientMap.emit("getChunk", ray);
        System.out.println("Test reçu sur lazybot-master !");
    }
}
