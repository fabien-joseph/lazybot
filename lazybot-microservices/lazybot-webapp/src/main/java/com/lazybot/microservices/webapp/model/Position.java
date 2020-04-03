package com.lazybot.microservices.webapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Position {
    Integer x;
    Integer y;
    Integer z;

    public Position() {
    }

    public Position(Integer x, Integer y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
