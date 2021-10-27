package cmpg.photoshare.entity;

import javax.persistence.*;

@Entity
public class Member {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "hashedPass")
    private String hashedPass;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPass() {
        return hashedPass;
    }

    public void setHashedPass(String hashedPass) {
        this.hashedPass = hashedPass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
