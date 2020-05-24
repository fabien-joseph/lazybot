package com.lazybot.microservices.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Login {
    private String nickname;
    private String password;
    private String server;

    public Login() {
    }

    public Login(String nickname, String password, String server) {
        this.nickname = nickname;
        this.password = password;
        this.server = server;
    }
}
