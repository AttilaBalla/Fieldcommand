package com.fieldcommand.intrequest;

import com.fieldcommand.user.User;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table( name = "int_requests" )
public class InternalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User owner;

    @NotBlank
    private String title;

    @NotBlank
    @Column(length = 150000)
    private String content;

    @Enumerated(EnumType.STRING)
    private InternalRequestStatus status;

    private String date = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());

    InternalRequest() {

    }

    public InternalRequest(String title, String content, User owner, InternalRequestStatus status) {
        this.title = title;
        this.content = content;
        this.owner = owner;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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
        return "InternalRequest: " +
                "id = " + id +
                ", owner = " + owner.getUsername() +
                ", title = " + title +
                ", content = " + content +
                ", date = " + date;
    }
}
