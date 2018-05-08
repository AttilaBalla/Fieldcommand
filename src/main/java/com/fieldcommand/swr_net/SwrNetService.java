package com.fieldcommand.swr_net;

import com.fieldcommand.utility.JsonUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.swing.text.html.HTMLDocument;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class SwrNetService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${swrnet.rotr-internal.url}")
    private String url;


    private JSONObject status;

    @Scheduled(fixedDelay = 60000)
    public void updateStatus() throws JSONException {
        status =  JsonUtil.getJson(url);
        logger.info("Updated SWR.net status -> {}", status);
    }

    public Map getStatus() throws JSONException {

        Map<String, String> status = new HashMap<>();

        Iterator keys = this.status.keys();

        while(keys.hasNext()) {
            String key = keys.next().toString();
            status.put(key, this.status.get(key).toString());
        }

        return status;
    }
}
