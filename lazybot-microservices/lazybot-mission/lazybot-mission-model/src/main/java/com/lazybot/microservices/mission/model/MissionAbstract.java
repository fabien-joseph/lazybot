package com.lazybot.microservices.mission.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Method;
import java.util.List;

/**
 * All missions possible
 * @param <T> Type of the data
 */
@Getter
@Setter
@ToString
public abstract class MissionAbstract<T> {
    private String nameMission;
    private int stepActual;
    private T data;
    private List<Method> steps;
    private List<Method> missionInitializer;

    public abstract void initializeSteps() throws NoSuchMethodException;

    public abstract void initializeMissions() throws NoSuchMethodException;

    public abstract void test();

    public final Method getStep(int step) {
        return this.getSteps().get(step);
    }
}
