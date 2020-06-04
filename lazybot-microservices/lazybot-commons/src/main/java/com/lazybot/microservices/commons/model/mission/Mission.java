package com.lazybot.microservices.commons.model.mission;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Object to do the mission with the name of the mission, the actual step and the datas.
 */
@Data
public class Mission {
    private int step;

    public Mission() {
    }

    public Mission(int step) {
        this.step = step;
    }
}
