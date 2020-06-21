package com.lazybot.microservices.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The position fo a {@link Bot}
 */
@Getter
@Setter
@ToString
public class Position {
    private double x;
    private double y;
    private double z;

    public Position() {

    }

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
