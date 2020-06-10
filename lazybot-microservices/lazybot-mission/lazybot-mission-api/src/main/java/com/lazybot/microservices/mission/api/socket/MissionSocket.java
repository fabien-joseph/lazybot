package com.lazybot.microservices.mission.api.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazybot.microservices.commons.model.mission.ExchangeMission;
import com.lazybot.microservices.commons.model.mission.Mission;
import com.lazybot.microservices.mission.business.MissionManager;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MissionSocket {
    private Map<Integer, Mission> missionsRunning;
    private int totalMissionSuccess;
    private int totalMissionFail;
    private SocketIOServer server;
    private final Socket socketMaster;
    private final MissionManager missionManager;

    public MissionSocket(SocketIOServer serverMission) throws URISyntaxException {
        this.server = serverMission;
        this.socketMaster = IO.socket("http://localhost:9090");
        this.socketMaster.connect();
        this.missionManager = new MissionManager();
        this.missionsRunning = new HashMap<>();
        this.totalMissionSuccess = 0;
        this.totalMissionFail = 0;

        // === Update existing mission
        server.addEventListener("missionDone", Integer.class, this::missionDone);
        server.addEventListener("missionFail", Integer.class, this::missionFail);

        // === Create a new mission
        server.addEventListener("exchange", String.class, this::exchangeMission);
    }

    private void missionDone(SocketIOClient socketIOClient, Integer missionId, AckRequest ackRequest) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Mission mission = missionsRunning.get(missionId);
        mission.setStep(mission.getStep() + 1);
        if (missionManager.runMission(socketMaster, mission)) {
            missionsRunning.remove(missionId);
            this.totalMissionSuccess++;
            System.out.println("Mission success count : " + totalMissionSuccess);
        }
        System.out.println("Mission total : " + missionsRunning.size());
    }

    private void missionFail(SocketIOClient socketIOClient, Integer missionId, AckRequest ackRequest) {
        missionsRunning.remove(missionId);
        this.totalMissionFail++;
        System.out.println("Mission fail count : " + totalMissionFail);
        System.out.println("Mission total : " + missionsRunning.size());
    }


    private void exchangeMission(SocketIOClient socketIOClient, String missionJson, AckRequest ackRequest) throws Exception {
        Type typeMission = new TypeToken<ExchangeMission>() {
        }.getType();
        ExchangeMission mission = (ExchangeMission) createMissionObject(missionJson, typeMission);

        missionsRunning.putIfAbsent(mission.getId(), mission);

        System.out.println("Nouvelle mission ! Total : " + missionsRunning.size());

        missionManager.runMission(socketMaster, mission);
    }

    private Mission createMissionObject(String missionJson, Type typeMission) {
        return new Gson().fromJson(missionJson, typeMission);
    }
}
