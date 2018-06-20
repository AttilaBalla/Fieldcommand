package com.fieldcommand.internal_request;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private String message;

    private String date = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(new Date());

    RequestModel() {
    }

    RequestModel(Long userId, String title, String message) {
        this.userId = userId;
        this.title = title;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getDate() {
        return date;
    }

    void setDate(String newDate) {
        this.date = newDate;
    }

    void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RequestModel{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

}
