package com.lazybot.microservices.mission.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import com.lazybot.microservices.commons.model.Mission;
import com.lazybot.microservices.mission.business.MissionManager;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.stereotype.Service;
import java.net.URISyntaxException;

@Service
public class MissionSocket {
    private SocketIOServer server;
    private final Socket socketMaster;
    private final MissionManager missionManager;

    public MissionSocket(SocketIOServer serverMission) throws URISyntaxException {
        this.server = serverMission;
        this.socketMaster = IO.socket("http://localhost:9090");
        this.socketMaster.connect();
        this.missionManager = new MissionManager();
        server.addEventListener("mission", String.class, this::exchangeMission);
    }

    private <T> void exchangeMission(SocketIOClient socketIOClient, String missionJson, AckRequest ackRequest) throws Exception {
        System.out.println("Exchange");
        Mission<T> mission = new Gson().fromJson(missionJson, Mission.class);
        missionManager.runMission(socketIOClient, mission);
    }
}
