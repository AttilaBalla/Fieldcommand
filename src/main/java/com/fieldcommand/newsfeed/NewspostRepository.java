package com.fieldcommand.newsfeed;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NewspostRepository extends CrudRepository<NewsPost, Long> {

    List<NewsPost> findAll();
}
