package com.lazybot.microservices.mission.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Method;
import java.util.List;

@Getter
@Setter
@ToString
public abstract class MissionTools<T> {
    private String nameMission;
    private int stepActual;
    private T mainData;
    private List<Method> steps;

    public abstract void initializeSteps() throws NoSuchMethodException;

    public final Method getStep(int step) {
        return this.getSteps().get(step);
    }
}
