package com.lazybot.microservices.mission.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lazybot.microservices.commons.model.Mission;
import com.lazybot.microservices.mission.business.MissionManager;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
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

    private void exchangeMission(SocketIOClient socketIOClient, String missionJson, AckRequest ackRequest) throws Exception {
        Mission<String> mission = executeMissionTest(missionJson);
        System.out.println("Exchange");
        missionManager.runMission(socketIOClient, mission);
    }


    private <T> Mission<T> executeMissionTest(String missionJson) {
        Type typePosition = new TypeToken<Mission<T>>() {}.getType();
        return new Gson().fromJson(missionJson, typePosition);
    }
}
