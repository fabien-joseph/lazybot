package com.lazybot.microservices.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Order<T> {
    private String botUsername;
    private T data;
    private String missionName;
    private int step;

    public Order() {
    }

    public Order(String botUsername, T data) {
        this.botUsername = botUsername;
        this.data = data;
    }

    public Order(String botUsername, T data, String missionName, int step) {
        this.botUsername = botUsername;
        this.data = data;
        this.missionName = missionName;
        this.step = step;
    }

    public Class<?> getTypeData() {
        return data.getClass();
    }
}
