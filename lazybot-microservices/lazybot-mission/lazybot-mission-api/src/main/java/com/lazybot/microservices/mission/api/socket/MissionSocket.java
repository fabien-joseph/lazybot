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
import java.util.concurrent.ThreadLocalRandom;

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

        // === FROM MASTER
        server.addEventListener("getMissionCounts", String.class, this::getMissionCounts);

        // === Update existing mission
        server.addEventListener("missionDone", Integer.class, this::missionDone);
        server.addEventListener("missionFail", Integer.class, this::missionFail);

        // === Create a new mission
        server.addEventListener("exchange", String.class, this::exchangeMission);
    }

    // === From Master MS

    /**
     * Return all the counts of the missions
     * @param socketIOClient Socket client
     * @param t unused parameter
     * @param ackRequest request informations
     */
    private void getMissionCounts(SocketIOClient socketIOClient, String t, AckRequest ackRequest) {
        returnTotalMissionRunning();
        returnTotalMissionFail();
        returnTotalMissionDone();
    }

    /**
     * Event receved when a step of a mission is ended. If there is a next step (mission can continue) do it, else if the mission is finished (don't have next step) remove the mission from {@link MissionSocket#missionsRunning}
     * @param socketIOClient Socket client
     * @param missionId id of the mission to update
     * @param ackRequest request informations
     */
    private void missionDone(SocketIOClient socketIOClient, Integer missionId, AckRequest ackRequest) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Mission mission = missionsRunning.get(missionId);
        mission.setStep(mission.getStep() + 1);
        if (missionManager.runMission(socketMaster, mission)) {
            missionsRunning.remove(missionId);
            this.totalMissionSuccess++;
            System.out.println("Mission success count : " + totalMissionSuccess);
        }
        returnTotalMissionDone();
        returnTotalMissionRunning();
        System.out.println("Mission running : " + missionsRunning.size());
    }

    /**
     * Event receved when a step of a mission fail. Delete the mission from {@link MissionSocket#missionsRunning}, increment the counter {@link MissionSocket#totalMissionFail} and send the new counters to the master
     * @param socketIOClient Socket client
     * @param missionId id of the mission to update
     * @param ackRequest request informations
     */
    private void missionFail(SocketIOClient socketIOClient, Integer missionId, AckRequest ackRequest) {
        missionsRunning.remove(missionId);
        this.totalMissionFail++;
        returnTotalMissionFail();
        returnTotalMissionRunning();
        System.out.println("Mission fail count : " + totalMissionFail);
        System.out.println("Mission running : " + missionsRunning.size());
    }

    /**
     * Execute the exchange mission
     * @param socketIOClient Socket client
     * @param missionJson {@link ExchangeMission} in JSON
     * @param ackRequest request informations
     */
    private void exchangeMission(SocketIOClient socketIOClient, String missionJson, AckRequest ackRequest) throws Exception {
        Type typeMission = new TypeToken<ExchangeMission>() {
        }.getType();
        ExchangeMission mission = (ExchangeMission) createMissionObject(missionJson, typeMission);

        missionsRunning.putIfAbsent(mission.getId(), mission);

        System.out.println("New mission ! Total : " + missionsRunning.size());
        returnTotalMissionRunning();
        missionManager.runMission(socketMaster, mission);
    }

    /**
     * Create a child object of {@link Mission}
     * @param missionJson {@link Mission} object JSON
     * @param typeMission The type of the mission object to return, example : {@link ExchangeMission}
     * @return The mission to do
     */
    private Mission createMissionObject(String missionJson, Type typeMission) {
        Mission mission = new Gson().fromJson(missionJson, typeMission);
        mission.setId(generadeMissionId());
        return mission;
    }

    /**
     * Send an event to the master to give the new count of missions running
     */
    private void returnTotalMissionRunning() {
        socketMaster.emit("updateTotalMissionRunning", missionsRunning.size());
    }

    /**
     * Send an event to the master to give the new count of missions done
     */
    private void returnTotalMissionDone() {
        socketMaster.emit("updateTotalMissionDone", totalMissionSuccess);
    }

    /**
     * Send an event to the master to give the new count of missions fail
     */
    private void returnTotalMissionFail() {
        socketMaster.emit("updateTotalMissionFail", totalMissionFail);
    }

    /**
     * Generate a random ID between 100.000 and 999.999
     */
    private int generadeMissionId() {
        return ThreadLocalRandom.current().nextInt(100000, 999999 + 1);
    }
}
