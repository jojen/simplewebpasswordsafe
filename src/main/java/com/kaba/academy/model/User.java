package com.kaba.academy.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "username", nullable = false)
    protected String username;

    @Column(name = "password", nullable = false)
    protected String password;

    @Column(name = "enabled", nullable = false)
    protected boolean enabled;

    public User(){}

    public User(String username, String password, boolean enabled) {
       setUsername(username);
        setPassword(password);
        setEnabled(enabled);
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected void setPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        this.password = passwordEncoder.encode(password);
    }

    protected void setUsername(String username){
        this.username = username;
    }
    protected void setEnabled(boolean enabled){
        this.enabled = enabled;
    }
}
