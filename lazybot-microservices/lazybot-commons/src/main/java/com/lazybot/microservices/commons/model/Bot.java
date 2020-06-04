package com.lazybot.microservices.commons.model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Bot {
    private String username;
    private String host;
    private Position position;
    private double health;
    private double food;
    private Inventory inventory;

    public Bot() {
    }

    public Bot(String username, String host, Position position, double health, double food, Inventory inventory) {
        this.username = username;
        this.host = host;
        this.position = position;
        this.health = health;
        this.food = food;
        this.inventory = inventory;
    }
}
