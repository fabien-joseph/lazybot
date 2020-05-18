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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        SimilaryStringManager ssm = new SimilaryStringManager();
        File folder = new File(getClass()
                .getResource("/static/textures").getPath());

        String path = "/static/textures/";
        path += ssm.testLevenshtein(nameObject, Objects.requireNonNull(folder.listFiles()));
        InputStream in = getClass().getResourceAsStream(path);
        return IOUtils.toByteArray(in);
    }

/*    @GetMapping("/getMCObjectTest/{nameObject}")
    public String getBlockTest(@PathVariable String nameObject) {
        SimilaryStringManager ssm = new SimilaryStringManager();
        File folder = new File(getClass()
                .getResource("/static/textures").getPath());
        System.out.println("BEST = " + );
        return "kikouPetitHiboux";
    }*/


}
