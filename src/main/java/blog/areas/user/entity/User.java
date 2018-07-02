package blog.areas.user.entity;

import blog.areas.article.entities.Article;
import blog.areas.role.entity.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    private Integer id;
    private String email;
    private String fullName;
    private String password;
    private Set<Role> roles;
    private Set<Article> articles;
    private byte[] picture;
    private Boolean isActive;
    private Boolean isConfirm;

    public User(String email, String fullName, String password) {
        this();
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.roles = new HashSet<>();
        this.articles = new HashSet<>();
    }
    public User() {
        this.isActive = true;
        this.isConfirm = false;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "email", unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "fullName", nullable = false)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "password", length = 60, nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles")
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role){
        this.roles.add(role);
    }

    @OneToMany(mappedBy = "author")
    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    @Column()
    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
    @Column(name = "isActive")
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Column(name = "isConfirm")
    public Boolean getConfirm() {
        return isConfirm;
    }

    public void setConfirm(Boolean confirm) {
        isConfirm = confirm;
    }


    @Transient
    public boolean isAdmin(){
        return this.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    @Transient
    public boolean isAuthor(Article article){
        return Objects.equals(this.getId(),article.getAuthor().getId());
    }

}
