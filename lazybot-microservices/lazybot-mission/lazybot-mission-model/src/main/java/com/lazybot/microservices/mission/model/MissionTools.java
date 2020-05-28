package com.lazybot.microservices.mission.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class MissionTools {
    private int stepActual;
    private int stepMax;

    public MissionTools() {
    }

    public MissionTools(int stepActual) {
        this.stepActual = stepActual;
    }
    public MissionTools(int stepActual, int stepMax) {
        this.stepActual = stepActual;
        this.stepMax = stepMax;
    }
}
