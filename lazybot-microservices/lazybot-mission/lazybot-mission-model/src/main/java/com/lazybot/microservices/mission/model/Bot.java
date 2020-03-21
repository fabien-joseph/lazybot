package com.lazybot.microservices.mission.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Bot {
    String username;
    String password;

    public Bot() {
    }

    public Bot(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
