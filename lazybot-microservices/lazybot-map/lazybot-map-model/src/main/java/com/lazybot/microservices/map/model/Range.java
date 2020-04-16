package com.lazybot.microservices.map.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Range {
    int xMin;
    int xMax;
    int zMin;
    int zMax;

    public Range() {
    }

    public Range(int xMin, int xMax, int zMin, int zMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }
}
