package com.fieldcommand.controller;

import com.fieldcommand.newsfeed.NewsPostService;
import com.fieldcommand.payload.newsfeed.NewsPostJson;
import com.fieldcommand.user.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public void addNewsPost(@RequestBody @Valid NewsPostJson newsPost, Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Long userId = userPrincipal.getId();

        newspostService.saveNewsPost(newsPost, userId);

        //TODO ERROR HANDLING

        this.logger.info("post: {}, id: {}",newsPost.toString(), userId);


    }

}
