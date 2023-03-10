package CarPark.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@MappedSuperclass
public abstract class User implements Serializable {
    @Id
    @Column(name = "userId", nullable = false)
    private String userId;
    @Column(name = "password")
    private String password;
    private byte[] salt;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isLogged;

    @ManyToOne
    private Complaint complaint2Inspect;

    @OneToMany
    private List<Complaint> complaints;


    public User(String userId, String password, byte[] salt, String email, String firstName, String lastName) {
        this.userId = userId;
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isLogged = false;
    }

    public User() {}

    public String getId() {
        return userId;
    }

    public Complaint getComplaint2Inspect() {
        return complaint2Inspect;
    }

    public void setComplaint2Inspect(Complaint complaint2Inspect) {
        this.complaint2Inspect = complaint2Inspect;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }
}
