package cmpg.photoshare.entity;

import javax.persistence.*;

@Entity
public class MemberImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name= "memberImageId")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "path")
    private String path;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
