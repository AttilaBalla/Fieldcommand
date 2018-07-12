package com.fieldcommand.intrequest;

import com.fieldcommand.project.Project;
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

    private String response;

    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
            name = "intrequest_user",
            joinColumns = { @JoinColumn(name = "request_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private Set<User> supportingUsers = new HashSet<>();

    private int supportPercent;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Project project;

    @Transient
    private String projectName;

    private String date = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());

    InternalRequest() {
    }

    public InternalRequest(String title, String content, User owner, InternalRequestStatus status, String projectName) {
        this.title = title;
        this.content = content;
        this.owner = owner;
        this.status = status;
        this.projectName = projectName;
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

    Set<User> getSupportingUsers() {
        return supportingUsers;
    }

    public void setSupportingUsers(Set<User> supportingUsers) {
        this.supportingUsers = supportingUsers;
    }

    int getSupportPercent() {
        return supportPercent;
    }

    void setSupportPercent(int supportPercent) {
        this.supportPercent = supportPercent;
    }

    public String getResponse() {
        return response;
    }

    void setResponse(String response) {
        this.response = response;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "InternalRequest: " +
                "id = " + id +
                ", owner = " + owner.getUsername() +
                ", title = " + title +
                ", content = " + content +
                ", date = " + date +
                ", supporting users = " + getSupportingUsers() +
                ", percent = " + getSupportPercent();
    }
}
