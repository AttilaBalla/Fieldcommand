package com.fieldcommand.internal_request;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class RequestModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @Enumerated(EnumType.STRING)
    private InternalRequestStatus status;

    private String date = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(new Date());

    RequestModel() {
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    void setUserId(Long userId) {
        this.userId = userId;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
    }

    String getDate() {
        return date;
    }

    void setDate(String newDate) {
        this.date = newDate;
    }

    public InternalRequestStatus getStatus() {
        return status;
    }

    void setStatus(InternalRequestStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RequestModel{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
