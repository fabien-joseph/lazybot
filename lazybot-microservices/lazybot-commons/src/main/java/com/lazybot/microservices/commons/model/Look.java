package com.lazybot.microservices.commons.model;

import lombok.Data;

/**
 * The yaw and pitch that a bot must look
 */
@Data
public class Look {
    double yaw;
    double pitch;

    public Look(double yaw, double pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
