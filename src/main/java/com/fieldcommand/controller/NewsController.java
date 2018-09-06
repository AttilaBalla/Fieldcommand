package com.fieldcommand.controller;

import com.fieldcommand.newsfeed.NewsPostService;
import com.fieldcommand.payload.GenericResponseJson;
import com.fieldcommand.payload.newsfeed.NewsPostJson;
import com.fieldcommand.user.UserPrincipal;
import com.fieldcommand.utility.Exception.UnauthorizedModificationException;
import com.fieldcommand.utility.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

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

    @GetMapping(value = "/api/getNewsPosts/{id}")
    public ResponseEntity<?> getNewsPost(@PathVariable("id")long id) {
        try {

            return ResponseEntity.status(200).body(JsonUtil.toJson(newspostService.findNewsPost(id)));

        } catch(NoSuchElementException ex) {

            return ResponseEntity.status(404).body(new GenericResponseJson(false));
        }
    }

    @PutMapping("/api/dev/updateNewsPost")
    public ResponseEntity<?> updateNewsPost(@RequestBody NewsPostJson newsPostJson, Authentication authentication) {

        GenericResponseJson response = new GenericResponseJson();
        try {
            newspostService.updateNewsPost(newsPostJson, authentication);

        } catch (NoSuchElementException | UnauthorizedModificationException ex) {

            response.setSuccess(false);
            response.setInformation(ex.getMessage());
            return ResponseEntity.badRequest().body(response);

        }

        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/dev/deleteNewsPost/{id}")
    public ResponseEntity<?> deleteNewsPost(@PathVariable("id")long id, Authentication authentication) {

        GenericResponseJson response = new GenericResponseJson();

        try {
            newspostService.deleteNewsPost(id, authentication);

        } catch (NoSuchElementException | UnauthorizedModificationException ex) {

            response.setSuccess(false);
            response.setInformation(ex.getMessage());
            return ResponseEntity.badRequest().body(response);

        }

        response.setSuccess(true);

        return ResponseEntity.ok(response);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private GenericResponseJson handleValidationErrors() {

        return new GenericResponseJson(false, "Validation error occured!");
    }

}
