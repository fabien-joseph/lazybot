package com.lazybot.microservices.map.api.socket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapSocket {
    private final SocketIONamespace namespace;

    @Autowired
    public MapSocket(SocketIOServer server) {
        this.namespace = server.addNamespace("/");
        server.addEventListener("loadChunk", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
                System.out.println("yes");
            }
        });
    }
}
