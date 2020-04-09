package com.lazybot.microservices.master.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class Request {
    private Object target;
    private String request;
    private Object reponse;
}
