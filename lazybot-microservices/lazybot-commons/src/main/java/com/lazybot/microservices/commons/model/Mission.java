package com.lazybot.microservices.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Mission<T> {
    private String missionName;
    private int step;
    private int stepMax;
    private T mainData;

    public Mission() {
    }

    public Mission(String missionName, int step, int stepMax, T mainData) {
        this.missionName = missionName;
        this.step = step;
        this.stepMax = stepMax;
        this.mainData = mainData;
    }
}
