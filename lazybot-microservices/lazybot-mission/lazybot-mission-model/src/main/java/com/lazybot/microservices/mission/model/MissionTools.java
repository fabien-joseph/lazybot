package com.lazybot.microservices.mission.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class MissionTools {
    private int stepActuel;
    private int stepMax;

    public MissionTools() {
    }

    public MissionTools(int stepActuel) {
        this.stepActuel = stepActuel;
    }
    public MissionTools(int stepActuel, int stepMax) {
        this.stepActuel = stepActuel;
        this.stepMax = stepMax;
    }
}
