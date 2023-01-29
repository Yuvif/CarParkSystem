package CarPark.entities;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class User implements Serializable {
    @Id
    @Column(name = "userId", nullable = false)
    private Long userId;
    @Column(name = "password")
    private String password;
    private byte[] salt;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isLogged;

    public User() {

    }

    public Long getId() {
        return userId;
    }

    public User(long userId, String password, byte[] salt, String email, String firstName, String lastName) {
        this.userId = userId;
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isLogged = false;
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

}
