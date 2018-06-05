package com.fieldcommand.newsfeed;

import com.fieldcommand.user.User;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "newsposts")
public class NewsPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User owner;

    private Timestamp timestamp;

    NewsPost() {}

    public NewsPost(String title, String content, User owner) {

        this.title = title;
        this.content = content;
        this.owner = owner;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
