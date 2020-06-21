package com.lazybot.microservices.mission.business;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import io.socket.client.Socket;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.lang.reflect.Method;
import java.util.List;


/**
 * Abstract class for all the managers of the missions.
 */
@Getter
@Setter
@ToString
public abstract class MissionAbstractManager {
    private Socket masterSocket;
    private List<Method> steps;
    private List<Method> missionInitializer;

    /**
     * Initialize the methods of the steps. They have to be added in {@link MissionAbstractManager#steps}
     * @throws NoSuchMethodException for reflection
     */
    public abstract void initializeSteps() throws NoSuchMethodException;

    /**
     * Return the method of the step to execute
     * @param step number of the step to do
     * @return {@link Method} the method of object to execute
     */
    public final Method getStep(int step) {
        return this.getSteps().get(step);
    }
}
