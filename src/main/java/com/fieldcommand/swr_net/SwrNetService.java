package com.fieldcommand.swr_net;

import com.fieldcommand.utility.JsonUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SwrNetService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${swrnet.rotr-internal.url}")
    private String url;


    private JSONObject status;

    @Scheduled(fixedDelay = 60000)
    public void updateStatus() throws JSONException {
        logger.info("Updating SWR.net status...");
        status =  JsonUtil.getJson(url);
    }

    public JSONObject getStatus() {
        return status;
    }
}
