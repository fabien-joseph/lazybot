package com.lazybot.microservices.mission.business;

import com.lazybot.microservices.commons.model.mission.Mission;
import io.socket.client.Socket;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MissionManager {

    public MissionManager() {
    }

    /**
     *
     * @param masterSocket
     * @param missionObject
     * @return true if the mission is finished, false is it isn't
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public boolean runMission(Socket masterSocket, Mission missionObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MissionAbstractManager mission = new ExchangeMissionManager();
        if (missionObject.getStep() < mission.getSteps().size()) {
            mission.setMasterSocket(masterSocket);
            Method m = mission.getStep(missionObject.getStep());
            m.invoke(mission, missionObject);
            return false;
        }
        return true;
    }
}
