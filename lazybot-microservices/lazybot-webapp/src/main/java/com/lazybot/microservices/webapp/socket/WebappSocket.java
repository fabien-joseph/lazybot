package com.lazybot.microservices.webapp.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
public class WebappSocket {
    private static final Logger log = LoggerFactory.getLogger(WebappSocket.class);
    private Socket socketMaster;

    public WebappSocket(SocketIOServer server) throws URISyntaxException {
        this.socketMaster = IO.socket("http://localhost:9090");
        this.socketMaster.connect();
        server.addConnectListener(onConnected());
        server.addEventListener("getChunk", Integer.class, this::getChunk);
    }

    private ConnectListener onConnected() {
        System.out.println("Un client vient de se connecter :o");
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.debug("Client[{}] - Connected to chat module through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
        };
    }

    public void getChunk(SocketIOClient client, Integer ray, AckRequest ackSender) {
        socketMaster.emit("getChunk", ray);
        System.out.println("Rayon = " + ray);
    }
}
