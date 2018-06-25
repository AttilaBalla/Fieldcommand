package com.fieldcommand.controller;

import com.fieldcommand.payload.GenericResponseJson;
import com.fieldcommand.swr_net.SwrNetService;
import com.fieldcommand.utility.JsonUtil;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SwrController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private SwrNetService swrNetService;

    @Autowired
    public void setUserService(SwrNetService swrNetService) {
        this.swrNetService = swrNetService;
    }

    @CrossOrigin
    @GetMapping(value = "/api/swrStatus")
    public String getSwrStatus() {
        try {

            return JsonUtil.toJson(swrNetService.getStatus());
        }
        catch (JSONException ex) {
            logger.warn("Error occured when retrieving SWR.net status: {}", ex.getMessage());

            return JsonUtil.toJson(new GenericResponseJson(false, "JSON error occured."));
        }
    }


}
