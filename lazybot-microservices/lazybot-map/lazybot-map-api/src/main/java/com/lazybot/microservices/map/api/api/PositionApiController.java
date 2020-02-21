package com.lazybot.microservices.map.api.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-02-10T10:49:33.117Z")

@Controller
public class PositionApiController implements PositionApi {

    private static final Logger log = LoggerFactory.getLogger(PositionApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public PositionApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<List<String>> getPositions(@NotNull @ApiParam(value = "Precise the quantity of bots you want. If it's many you'll have to give a list of the id of the bots that you want, if it's all all the bots will be returned", required = true, allowableValues = "many, all") @Valid @RequestParam(value = "quantity", required = true) List<String> quantity,@ApiParam(value = "Status values that need to be considered for filter") @Valid @RequestParam(value = "botsId", required = false) List<Integer> botsId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<String>>(objectMapper.readValue("[ \"\", \"\" ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<String>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<String>> goToPosition(@NotNull @ApiParam(value = "Give the list of id of bots that will have to go to the position", required = true) @Valid @RequestParam(value = "bots", required = true) List<Integer> bots,@ApiParam(value = "Status values that need to be considered for filter") @Valid @RequestParam(value = "position", required = false) List<Integer> position) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<String>>(objectMapper.readValue("[ \"\", \"\" ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<String>>(HttpStatus.NOT_IMPLEMENTED);
    }

}
