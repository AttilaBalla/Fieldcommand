package com.fieldcommand.newsfeed;

import java.util.List;

public interface NewspostRepository {

    List<Newspost> findAll();
}
