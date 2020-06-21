package com.lazybot.microservices.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Order that a bot must execute
 * @param <T> The datas to give to the bot
 */
@Getter
@Setter
@ToString
public class OrderBot<T> {
    /**
     * The mission ID which is actually running by a must (null if no mission is running)
     */
    private int missionId;
    /**
     * The username of a bot
     */
    private String botUsername;
    /**
     * The data to transmit to the bot
     */
    private T data;
    /**
     * The actual step running of a mission
     */
    private int step;

    public OrderBot() {
    }

    public OrderBot(String botUsername, T data) {
        this.botUsername = botUsername;
        this.data = data;
    }

    public OrderBot(int missionId, String botUsername, T data, int step) {
        this.missionId = missionId;
        this.botUsername = botUsername;
        this.data = data;
        this.step = step;
    }

    public Class<?> getTypeData() {
        return data.getClass();
    }
}
