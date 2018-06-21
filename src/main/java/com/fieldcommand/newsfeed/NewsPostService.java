package com.fieldcommand.newsfeed;

import com.fieldcommand.payload.newsfeed.NewsPostJson;
import com.fieldcommand.user.User;
import com.fieldcommand.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class NewsPostService {

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

    public List<HashMap<String, String>> findAll() {
        List<NewsPost> newsPosts = newspostRepository.findAllByOrderByIdDesc();
        List<HashMap<String, String>> newsPostData = new ArrayList<>();

        for (NewsPost newsPost: newsPosts) {
            newsPostData.add(makeNewsPostHashMap(newsPost));
        }

        return newsPostData;
    }

    public HashMap<String, String> findNewsPost(long id) {

        NewsPost newsPost = newspostRepository.findOne(id);

        return makeNewsPostHashMap(newsPost);
    }

    private HashMap<String, String> makeNewsPostHashMap(NewsPost newsPost) {
        HashMap<String, String> newsPostHashMap = new HashMap<>();

        newsPostHashMap.put("id", newsPost.getId().toString());
        newsPostHashMap.put("title", newsPost.getTitle());
        newsPostHashMap.put("content", newsPost.getContent());
        newsPostHashMap.put("owner", newsPost.getOwner().getUsername());
        newsPostHashMap.put("date", newsPost.getTimestamp());
        newsPostHashMap.put("visible", (newsPost.isVisibility()) ? "True" : "False");

        return newsPostHashMap;
    }
}
