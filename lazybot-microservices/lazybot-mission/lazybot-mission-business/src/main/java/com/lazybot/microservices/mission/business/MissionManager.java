package com.lazybot.microservices.mission.business;

import com.corundumstudio.socketio.SocketIOClient;
import com.lazybot.microservices.commons.model.Mission;
import com.lazybot.microservices.mission.model.MissionAbstract;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        MissionAbstract<T> missionRunning = getMissionClass(mission);

        Class c = missionRunning.getClass();

        List<String> str = new ArrayList<>();
        str.add("Bonjour1");
        str.add("Bonjour2");
        str.add("Bonjour3");

        System.out.println(missionRunning.getStep(mission.getStep()).getName());
        missionRunning.getStep(mission.getStep()).invoke(missionRunning, str);
    }

    private <T> MissionAbstract<T> getMissionClass (Mission<T> mission) throws NoSuchMethodException {
        if (mission.getMissionName().equals("exchange")) {
            return new Exchange<T>();
        }
        throw new NoSuchMethodException();
    }
}
