package com.lazybot.microservices.mission.business;

import com.lazybot.microservices.commons.model.mission.ExchangeMission;
import com.lazybot.microservices.commons.model.mission.Mission;
import io.socket.client.Socket;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MissionManager {

    public MissionManager() {
    }

    /**
     *  Execute the step to do of a mission if it is not finished.
     * @param masterSocket The socket of the Master MS
     * @param missionObject {@link Mission} object which has all the information to do the mission
     * @return false if a mission isn't finished, true if a mission if finished
     * @throws NoSuchMethodException for reflection
     * @throws InvocationTargetException for reflection
     * @throws IllegalAccessException for reflection
     * @throws ClassNotFoundException for reflection
     */
    public boolean runMission(Socket masterSocket, Mission missionObject) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        MissionAbstractManager mission = getMission(missionObject);
        if (missionObject.getStep() < mission.getSteps().size()) {
            mission.setMasterSocket(masterSocket);
            Method m = mission.getStep(missionObject.getStep());
            m.invoke(mission, missionObject);
            return false;
        }
        return true;
    }

    /**
     * List of all childs of {@link Mission}. This list has to be updated when a new mission is added !
     * @param mission {@link Mission}
     * @return the child of {@link Mission}
     * @throws NoSuchMethodException for reflection
     * @throws ClassNotFoundException for reflection
     */
    private MissionAbstractManager getMission(Mission mission) throws NoSuchMethodException, ClassNotFoundException {
        String className = mission.getClass().getName();
        if (ExchangeMission.class.getName().equals(className)) {
            return new ExchangeMissionManager();
        }
        throw new ClassNotFoundException();
    }
}
