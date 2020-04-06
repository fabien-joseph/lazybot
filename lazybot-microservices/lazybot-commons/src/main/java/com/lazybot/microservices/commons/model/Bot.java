package com.lazybot.microservices.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Bot {
    String username;
    Position position;
    int life;
    int food;

    public Bot() {
    }

    public Bot(String username, Position position, int life, int food) {
        this.username = username;
        this.position = position;
        this.life = life;
        this.food = food;
    }
}
