package com.lazybot.microservices.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderBot<T> {
    private int missionId;
    private String botUsername;
    private T data;
    private String missionName;
    private int step;

    public OrderBot() {
    }

    public OrderBot(String botUsername, T data) {
        this.botUsername = botUsername;
        this.data = data;
    }

    public OrderBot(int missionId, String botUsername, T data, String missionName, int step) {
        this.missionId = missionId;
        this.botUsername = botUsername;
        this.data = data;
        this.missionName = missionName;
        this.step = step;
    }

    public Class<?> getTypeData() {
        return data.getClass();
    }
}