package com.lazybot.microservices.mission.business;

import com.corundumstudio.socketio.SocketIOClient;
import com.lazybot.microservices.commons.model.Mission;
import com.lazybot.microservices.mission.model.MissionTools;

public class MissionManager {

    public MissionManager() {
    }

    /**
     * Run all the missions
     * @param masterSocket socket to connect with the master
     * @param mission {@link Mission}
     * @param <T> Type of the data to run the mission ({@link String}, {@link com.lazybot.microservices.commons.model.Position}, etc...)
     */
    public <T> void runMission(SocketIOClient masterSocket, Mission<T> mission) throws Exception {
        MissionTools<T> missionRunning = getMissionClass(mission);
        missionRunning.getStep(mission.getStep()).invoke(masterSocket, mission.getData());
    }

    private <T> MissionTools<T> getMissionClass (Mission<T> mission) throws NoSuchMethodException {
        if (mission.getMissionName().equals("exchange")) {
            return new Exchange<T>();
        }
        throw new NoSuchMethodException();
    }
}
