package com.fieldcommand.newsfeed;

import com.fieldcommand.payload.newsfeed.NewsPostJson;
import com.fieldcommand.user.User;
import com.fieldcommand.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
