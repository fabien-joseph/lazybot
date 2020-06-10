package com.lazybot.microservices.commons.model;

import lombok.Data;

@Data
public class Look {
    double yaw;
    double pitch;

    public Look(double yaw, double pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
