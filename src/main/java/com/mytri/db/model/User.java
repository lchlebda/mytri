package com.mytri.db.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @Column(name="name")
    @Getter @Setter
    @JsonValue
    private String name;

    @OneToMany(mappedBy = "user")
    @Getter @Setter
    private List<Activity> activities;

    public User() {}
}
