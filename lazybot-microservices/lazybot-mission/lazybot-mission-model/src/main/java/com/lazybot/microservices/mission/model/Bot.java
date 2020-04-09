package com.lazybot.microservices.mission.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;

@Getter
@Setter
@ToString
public class Bot {
    String username;
    String password;
    Process process;

    public Bot() {
    }

    public Bot(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void start() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("node",
                "C:\\Users\\Fabien\\IdeaProjects\\lazybot\\lazybot-robot\\robot.js",
                "--username=" + this.getUsername(),
                "--password=" + this.getPassword());
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        this.process = pb.start();
    }

    public void stop () {
        this.process.destroy();
    }
}
