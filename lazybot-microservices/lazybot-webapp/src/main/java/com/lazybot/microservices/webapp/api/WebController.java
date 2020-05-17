package com.lazybot.microservices.webapp.api;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;

@Controller
public class WebController {
    @GetMapping("/")
    public String home(Model model) {
        return "dashboard";
    }

    @GetMapping("/bot")
    public String bot(Model model) {
        return "botPanel";
    }

    @GetMapping("/test")
    public String test(Model model) {
        return "test";
    }

    @GetMapping("/getMCObject/{nameObject}")
    public @ResponseBody byte[] getBlock(@PathVariable String nameObject) throws IOException {
        String path = "/static/textures/";
        path += nameObject + ".png";
        InputStream in = getClass().getResourceAsStream(path);
        return IOUtils.toByteArray(in);
    }
}
