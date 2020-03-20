package com.lazybot.microservices.mission.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissionSocket {

    public MissionSocket(SocketIOServer server) {
        server.addEventListener("doFight", List.class, this::myMethod);
    }

    public void myMethod (SocketIOClient client, List<Integer> data, AckRequest ackSender) throws Exception {
        System.out.println(data.size());
    }
}
