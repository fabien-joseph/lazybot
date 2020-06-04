package com.lazybot.microservices.mission.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazybot.microservices.commons.model.Bot;
import com.lazybot.microservices.commons.model.mission.ExchangeMission;
import com.lazybot.microservices.commons.model.mission.Mission;
import com.lazybot.microservices.mission.business.MissionManager;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.List;

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
        server.addEventListener("exchange", String.class, this::exchangeMission);
    }

    private void exchangeMission(SocketIOClient socketIOClient, String missionJson, AckRequest ackRequest) throws Exception {
        Type typeMission = new TypeToken<ExchangeMission>() {}.getType();
        ExchangeMission mission = (ExchangeMission) createMissionObject(missionJson, typeMission);
        missionManager.runMission(socketIOClient, mission);
    }

    private Mission createMissionObject(String missionJson, Type typeMission) {
        return new Gson().fromJson(missionJson, typeMission);
    }
}
