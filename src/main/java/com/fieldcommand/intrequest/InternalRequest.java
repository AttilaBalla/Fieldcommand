package com.fieldcommand.intrequest;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table( name = "int_requests" )
public class InternalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    @NotBlank
    private String title;

    @NotBlank
    @Column(length = 150000)
    private String content;

    @Enumerated(EnumType.STRING)
    private InternalRequestStatus status;

    private String date = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());

    InternalRequest(Long userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.status = InternalRequestStatus.WAITING;
    }

    InternalRequest() {

    }

    public Long getId() {
        return id;
    }

    Long getUserId() {
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

    InternalRequestStatus getStatus() {
        return status;
    }

    void setStatus(InternalRequestStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InternalRequest{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
