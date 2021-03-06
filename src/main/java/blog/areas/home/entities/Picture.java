package blog.areas.home.entities;

import blog.areas.user.entity.User;

import javax.persistence.*;

@Entity
@Table(name = "pictures")
public class Picture {
    private Integer id;
    private byte[] picture;
    private User user;

    public Picture(byte[] picture) {
        this.picture = picture;
    }


    @ManyToOne
    @JoinColumn(nullable = false, name = "authorId")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }


}
