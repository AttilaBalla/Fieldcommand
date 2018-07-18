package com.fieldcommand.payload.newsfeed;

import org.hibernate.validator.constraints.NotBlank;

public class NewsPostJson {

    private long Id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private boolean visible;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "id: " + Id + "title: " + title + " content: " + content + " visible: " + visible;
    }
}
