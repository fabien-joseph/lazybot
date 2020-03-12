package com.lazybot.microservices.map.api.socket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.lazybot.microservices.map.business.CellManager;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MapSocket {
    private final CellManager cellManager;

    public MapSocket(SocketIOServer server, CellManager cellManager) {
        server.addEventListener("loadChunk", List.class, this::myMethod);
        this.cellManager = cellManager;
    }

    public void myMethod (SocketIOClient client, List<Integer> data, AckRequest ackSender) throws Exception {

        System.out.println(data.size());
    }
}
