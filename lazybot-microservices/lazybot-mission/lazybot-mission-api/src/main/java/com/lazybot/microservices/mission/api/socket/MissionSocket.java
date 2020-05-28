package com.lazybot.microservices.mission.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.lazybot.microservices.mission.model.Exchange;
import com.lazybot.microservices.mission.model.MissionTools;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.net.URISyntaxException;

@Service
public class MissionSocket {
    private Socket socketMaster;

    public MissionSocket(SocketIOServer server) throws URISyntaxException {
        this.socketMaster = IO.socket("http://localhost:9090");
        this.socketMaster.connect();
        registrateToMaster();
        server.addEventListener("exchange", MissionTools.class, this::exchangeMission);
    }

    private void exchangeMission(SocketIOClient socketIOClient, MissionTools mission, AckRequest ackRequest) throws NoSuchMethodException {
        Exchange exchange = new Exchange();
        Method m = exchange.doStep(5);
    }

    private void registrateToMaster() {
        socketMaster.emit("connectMission");
    }
}
