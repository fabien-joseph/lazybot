package com.lazybot.microservices.commons.manager;

public class ToolsBotManager {
    /**
     * Correct the username bot to delete the "/" character in fist position.
     * @param botUsername username of the bot
     * @return void
     */
    public String correctBotUsername(String botUsername) {
        return botUsername.substring(1);
    }

    public boolean isBeginningWithWrongChar(String botUsername) {
        return botUsername.charAt(0) == '/';
    }

    public boolean isLegitBotUsername(String botUsername) {
        return botUsername.matches("[a-zA-Z0-9_]{1,16}");
    }
}
