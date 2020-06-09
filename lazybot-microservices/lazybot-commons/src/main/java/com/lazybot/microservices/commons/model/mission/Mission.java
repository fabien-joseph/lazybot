package com.lazybot.microservices.commons.model.mission;

import lombok.Data;

/**
 * Object to do the mission with the name of the mission, the actual step and the datas.
 */
@Data
public class Mission {
    private int id;
    private int step;

    public Mission() {
    }

    public Mission(int step) {
        this.step = step;
    }

    public Mission(int id, int step) {
        this.id = id;
        this.step = step;
    }


}
