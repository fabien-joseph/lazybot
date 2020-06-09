package com.lazybot.microservices.master.model;


import com.corundumstudio.socketio.SocketIOClient;
import com.lazybot.microservices.commons.model.Bot;
import lombok.Data;

import java.util.Collections;

@Data
public class BotIdentifier {
    private SocketIOClient socketIOClient;
    private Bot bot;
}
