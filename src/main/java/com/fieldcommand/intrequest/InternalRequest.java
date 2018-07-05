package com.fieldcommand.intrequest;

import com.fieldcommand.user.User;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table( name = "int_requests" )
public class InternalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
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


    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
            name = "intrequest_user",
            joinColumns = { @JoinColumn(name = "request_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private Set<User> supportingUsers = new HashSet<>();

    private int supportPercent;

    private String project;

    private String date = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());

    InternalRequest() {

    }

    public InternalRequest(String title, String content, User owner, InternalRequestStatus status, String project) {
        this.title = title;
        this.content = content;
        this.owner = owner;
        this.status = status;
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    User getOwner() {
        return owner;
    }

    void setOwner(User owner) {
        this.supportingUsers.add(owner);
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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    Set<User> getSupportingUsers() {
        return supportingUsers;
    }

    public void setSupportingUsers(Set<User> supportingUsers) {
        this.supportingUsers = supportingUsers;
    }

    public int getSupportPercent() {
        return supportPercent;
    }

    public void setSupportPercent(int supportPercent) {
        this.supportPercent = supportPercent;
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
