package com.lazybot.microservices.master.model;


import com.corundumstudio.socketio.SocketIOClient;
import com.lazybot.microservices.commons.model.Bot;
import lombok.Data;

import java.util.Collections;

/**
 * Informations of a bot
 */
@Data
public class BotIdentifier {
    /**
     * The socket client of a bot
     */
    private SocketIOClient socketIOClient;
    /**
     * The {@link Bot} object of a bot
     */
    private Bot bot;
}
