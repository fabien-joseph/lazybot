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

    public abstract void initializeSteps() throws NoSuchMethodException;

    public final Method getStep(int step) {
        return this.getSteps().get(step);
    }
}
