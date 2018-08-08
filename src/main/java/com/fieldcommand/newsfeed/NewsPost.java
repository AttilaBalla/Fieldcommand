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

    private String summary;

    @Column(length = 150000)
    private String content;

    private boolean visibility;

    private boolean deleted = false;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User owner;

    private String timestamp;

    NewsPost() {}

    public NewsPost(String title, String summary, String content, User owner, boolean visibility) {

        this.title = title;
        this.summary = summary;
        this.content = content;
        this.owner = owner;
        this.visibility = visibility;
        timestamp = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
    }

    User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    boolean isVisibility() {
        return visibility;
    }

    void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    boolean isDeleted() {
        return deleted;
    }

    void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
