package com.lazybot.microservices.map.api.socket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MapSocket {
    private final SocketIONamespace namespace;

    @Autowired
    public MapSocket(SocketIOServer server) {
        this.namespace = server.addNamespace("/");
        server.addEventListener("loadChunk", List.class, new DataListener<List>() {
            @Override
            public void onData(SocketIOClient client, List data, AckRequest ackSender) throws Exception {
                System.out.println(data.size());
            }
        });
    }
}
