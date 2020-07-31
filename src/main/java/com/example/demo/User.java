package com.example.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany (mappedBy = "user")
    private Set<Course> courses;

    @Column (name = "username")
    private String username;    //real name also username

    @Column (name = "password")
    private String password;

    @Column (name = "enabled")
    private boolean enabled;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

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
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void clearPassword(){
        this.password="";
    }

    public void enrollCourse(Course course){
        if (this.courses==null){
            this.courses=new HashSet<Course>();
        }
        this.courses.add(course);
    }



}
