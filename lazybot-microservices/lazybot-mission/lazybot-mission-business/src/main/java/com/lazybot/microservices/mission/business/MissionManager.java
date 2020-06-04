package com.lazybot.microservices.mission.business;

import com.corundumstudio.socketio.SocketIOClient;
import com.lazybot.microservices.commons.model.mission.Mission;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MissionManager<T> {

    public MissionManager() {
    }

    /**
     * Run all the missions
     * @param masterSocket socket to connect with the master
     * @param missionObject {@link Mission}
     */
    public void runMission(SocketIOClient masterSocket, Mission missionObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MissionAbstractManager mission = new ExchangeMissionManager();
        mission.setMasterSocket(masterSocket);
        Method m = mission.getStep(missionObject.getStep());
        m.invoke(mission, missionObject);
    }
}
