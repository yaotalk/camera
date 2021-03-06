package com.minivision.cameraplat.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ClientUser extends IdEntity {

    @Column(unique = true,nullable = false)
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override public String toString() {
        return "clientUser{" + "userId='" + id  + ", username='" + username + '\'' + ", password='" + password + '\''
            + '}';
    }
}
