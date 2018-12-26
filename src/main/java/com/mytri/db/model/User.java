package com.mytri.db.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @Column(name="name")
    @Getter @Setter
    private String name;

    @OneToMany(mappedBy = "user")
    @Getter @Setter
    private List<Activity> activities;

    public User() {}
}
