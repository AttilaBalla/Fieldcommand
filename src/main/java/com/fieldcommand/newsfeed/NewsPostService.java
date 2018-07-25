package com.fieldcommand.newsfeed;

import com.fieldcommand.payload.newsfeed.NewsPostJson;
import com.fieldcommand.role.RoleType;
import com.fieldcommand.user.User;
import com.fieldcommand.user.UserRepository;
import com.fieldcommand.utility.Exception.UnauthorizedModificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class NewsPostService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserRepository userRepository;
    private NewsPostRepository newspostRepository;

    @Autowired
    public NewsPostService(NewsPostRepository newspostRepository, UserRepository userRepository) {
        this.newspostRepository = newspostRepository;
        this.userRepository = userRepository;
    }

    public void saveNewsPost(NewsPostJson newsPost, Long userId) {

        User owner = userRepository.findUserById(userId);

        newspostRepository.save(new NewsPost(newsPost.getTitle(), newsPost.getContent(), owner, newsPost.isVisible()));

    }

    public List<HashMap<String, Object>> findAll() {
        List<NewsPost> newsPosts = newspostRepository.findAllByDeletedFalseOrderByIdDesc();
        List<HashMap<String, Object>> newsPostData = new ArrayList<>();

        for (NewsPost newsPost: newsPosts) {
            newsPostData.add(makeNewsPostHashMap(newsPost));
        }

        return newsPostData;
    }

    public HashMap<String, Object> findNewsPost(long id) throws NoSuchElementException {

        NewsPost newsPost = newspostRepository.findOne(id);

        if(newsPost == null || newsPost.isDeleted() ) {
            throw new NoSuchElementException();
        }

        return makeNewsPostHashMap(newsPost);
    }

    private HashMap<String, Object> makeNewsPostHashMap(NewsPost newsPost) {
        HashMap<String, Object> newsPostHashMap = new HashMap<>();

        newsPostHashMap.put("id", newsPost.getId().toString());
        newsPostHashMap.put("title", newsPost.getTitle());
        newsPostHashMap.put("owner", newsPost.getOwner().getUsername());
        newsPostHashMap.put("date", newsPost.getTimestamp());
        newsPostHashMap.put("visible", (newsPost.isVisibility()));
        newsPostHashMap.put("content", newsPost.getContent());

        return newsPostHashMap;
    }

    public void updateNewsPost(NewsPostJson newsPostJson, Authentication authentication)
            throws UnauthorizedModificationException {

        User updater = userRepository.findUserByUsername(authentication.getName());

        NewsPost newsPost = newspostRepository.findOne(newsPostJson.getId());

        if(newsPost == null) {
            throw new NoSuchElementException();
        }

        if(updater.getRole().getRoleType() == RoleType.ROLE_DEVELOPER &&
                !newsPost.getOwner().getUsername().equals(updater.getUsername())) {
            throw new UnauthorizedModificationException("You are not allowed to edit this post!");
        }

        newsPost.setTitle(newsPostJson.getTitle());
        newsPost.setContent(newsPostJson.getContent());
        newsPost.setVisibility(newsPostJson.isVisible());

        newspostRepository.save(newsPost);

    }

    public void deleteNewsPost(long id, Authentication authentication) throws UnauthorizedModificationException {

        User updater = userRepository.findUserByUsername(authentication.getName());

        NewsPost newsPost = newspostRepository.findOne(id);

        if(newsPost == null) {
            throw new NoSuchElementException();
        }

        if(updater.getRole().getRoleType() == RoleType.ROLE_DEVELOPER &&
                !newsPost.getOwner().getUsername().equals(updater.getUsername())) {
            throw new UnauthorizedModificationException("You are not allowed to delete this post!");
        }

        newsPost.setDeleted(true);

        newspostRepository.save(newsPost);

    }
}
