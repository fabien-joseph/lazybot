package com.lazybot.microservices.mission.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.lazybot.microservices.commons.model.Mission;
import com.lazybot.microservices.mission.business.MissionManager;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.stereotype.Service;
import java.net.URISyntaxException;

@Service
public class MissionSocket {
    private SocketIOServer server;
    private Socket socketMaster;
    private MissionManager missionManager;

    public MissionSocket(SocketIOServer serverMaster) throws URISyntaxException {
        this.socketMaster = IO.socket("http://localhost:9090");
        this.socketMaster.connect();
        this.server = serverMaster;
        this.missionManager = new MissionManager();
        registrateToMaster();
        server.addEventListener("exchange", Mission.class, this::exchangeMission);
    }

    private <T> void exchangeMission(SocketIOClient socketIOClient, Mission<T> mission, AckRequest ackRequest) {

    }

    private void registrateToMaster() {
        socketMaster.emit("connectMission");
    }
}
