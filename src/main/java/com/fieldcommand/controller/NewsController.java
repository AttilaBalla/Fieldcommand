package com.fieldcommand.controller;

import com.fieldcommand.newsfeed.NewsPostService;
import com.fieldcommand.payload.GenericResponseJson;
import com.fieldcommand.payload.newsfeed.NewsPostJson;
import com.fieldcommand.user.UserPrincipal;
import com.fieldcommand.utility.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class NewsController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private NewsPostService newspostService;

    @Autowired
    public NewsController(NewsPostService newsPostService) {
        this.newspostService = newsPostService;
    }

    @PostMapping("/api/dev/addNewsPost")
    public ResponseEntity<?> addNewsPost(@RequestBody @Valid NewsPostJson newsPost, Authentication authentication) {

        GenericResponseJson response = new GenericResponseJson();

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Long userId = userPrincipal.getId();

        newspostService.saveNewsPost(newsPost, userId);

        //TODO ERROR HANDLING

        response.setSuccess(true);
        return ResponseEntity.ok(response);

    }

    @GetMapping(value = "/api/getNewsPosts")
    public String getNewsPosts() {

        return JsonUtil.toJson(newspostService.findAll());
    }

    @GetMapping(value = "/api/dev/getNewsPosts/{id}")
    public String getNewsPosts(@PathVariable("id")long id) {

        return JsonUtil.toJson(newspostService.findNewsPost(id));
    }

}
