package com.lazybot.microservices.map.api.lazybotmapapi.socket;

import io.socket.client.IO;
import org.springframework.stereotype.Service;
import io.socket.client.Socket;
import java.net.URISyntaxException;

@Service
public class MapSocket {

    private Socket socketMaster;

    public MapSocket() throws URISyntaxException {
        this.socketMaster = IO.socket("http://localhost:9090");
        this.socketMaster.connect();
        registrateToMaster();
        this.socketMaster.on("test", this::testMethod);
    }

    private void testMethod(Object... args) {
        System.out.println(args.length);
    }

    private void registrateToMaster() {
        socketMaster.emit("connectMap");
    }
}
