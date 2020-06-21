package com.lazybot.microservices.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Connection informations
 */
@Getter
@Setter
@ToString
public class Login {
    /**
     * Username of the bot
     */
    private String nickname;
    /**
     * The password of the bot. Had to be used if we want to connect a premium account
     */
    private String password;
    /**
     * The server where the bot must be connected
     */
    private String server;

    public Login() {
    }

    public Login(String nickname, String password, String server) {
        this.nickname = nickname;
        this.password = password;
        this.server = server;
    }
}
