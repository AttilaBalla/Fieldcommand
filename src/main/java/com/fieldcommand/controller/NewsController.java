package com.fieldcommand.controller;

import com.fieldcommand.newsfeed.NewsPost;
import com.fieldcommand.newsfeed.NewspostRepository;
import com.fieldcommand.payload.newsfeed.NewsPostJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewsController {

    private NewspostRepository newspostRepository;

    @Autowired
    public NewsController(NewspostRepository newspostRepository) {
        this.newspostRepository = newspostRepository;
    }

    @PostMapping("/api/admin/addNewsPost")
    public void addNewsPost(@RequestBody NewsPostJson newsPost) {

        //newspostRepository.save(new NewsPost(newsPost.getTitle(), newsPost.getContent(), ));

    }

}
