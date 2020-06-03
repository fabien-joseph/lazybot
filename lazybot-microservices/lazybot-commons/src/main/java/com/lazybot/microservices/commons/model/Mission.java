package com.lazybot.microservices.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Object to do the mission with the name of the mission, the actual step and the datas.
 * @param <T> Type of the object data
 */
@Getter
@Setter
@ToString
public class Mission<T> {
    private String missionName;
    private int step;
    private T data;

    public Mission() {
    }

    public Mission(String missionName, int step, T data) {
        this.missionName = missionName;
        this.step = step;
        this.data = data;
    }
}
