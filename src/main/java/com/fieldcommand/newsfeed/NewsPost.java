package com.fieldcommand.newsfeed;

import com.fieldcommand.user.User;

import javax.persistence.*;
import java.util.Date;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "newsposts")
public class NewsPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private boolean visibility;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User owner;

    private String timestamp;

    NewsPost() {}

    NewsPost(String title, String content, User owner, boolean visibility) {

        this.title = title;
        this.content = content;
        this.owner = owner;
        this.visibility = visibility;
        timestamp = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").format(new Date());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
