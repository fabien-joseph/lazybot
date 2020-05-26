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

    public Order() {
    }

    public Order(String botUsername, T data) {
        this.botUsername = botUsername;
        this.data = data;
    }

    public Class<?> getTypeData() {
        return data.getClass();
    }
}
