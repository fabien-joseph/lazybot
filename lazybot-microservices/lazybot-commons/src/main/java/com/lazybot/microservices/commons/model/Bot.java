package com.lazybot.microservices.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Bot {
    int id;
    String username;
    Position position;
    int health;
    int food;

    public Bot() {
    }

    public Bot(int id, String username, Position position, int health, int food) {
        this.id = id;
        this.username = username;
        this.position = position;
        this.health = health;
        this.food = food;
    }
}
