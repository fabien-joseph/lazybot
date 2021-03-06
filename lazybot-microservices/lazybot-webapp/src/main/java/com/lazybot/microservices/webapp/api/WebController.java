package com.lazybot.microservices.webapp.api;

import com.lazybot.microservices.webapp.business.SimilaryStringManager;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Controller
public class WebController {
    /**
     * Dashboard
     * @param model model of the page
     * @return return the page
     */
    @GetMapping("/")
    public String home(Model model) {
        return "dashboard";
    }

    /**
     * Go to the page panel of a bot
     * @param model model of the page
     * @param botUsername username of the bot
     * @return return the page
     */
    @GetMapping("/{botUsername}")
    public String bot(Model model, @PathVariable String botUsername) {
        return "botPanel";
    }

    /**
     * Endpoint to get the texture of an item
     * @param nameObject name of the item to get the texture
     * @return return the page
     * @throws IOException error if there is a problem during get the good texture file
     */
    @GetMapping("/getMCObject/{nameObject}")
    public @ResponseBody byte[] getBlock(@PathVariable String nameObject) throws IOException {
        SimilaryStringManager ssm = new SimilaryStringManager();
        File folder = new File(getClass()
                .getResource("/static/textures").getPath());

        String path = "/static/textures/";
        String file = ssm.testLevenshtein(nameObject, Objects.requireNonNull(folder.listFiles()));
        path += file;
        InputStream in = getClass().getResourceAsStream(path);
        return IOUtils.toByteArray(in);
    }
}
