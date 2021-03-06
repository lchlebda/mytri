package com.mytri.db.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mytri.db.enums.Sport;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name="ID")
    @Getter @Setter private Long id;

    @ManyToOne
    @JoinColumn(name="user_name")
    @Getter @Setter private User user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-ddThh:mm")
    @NotNull
    @Getter @Setter private LocalDateTime date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Getter @Setter private Sport sport;

    @NotNull
    @Getter @Setter private Integer duration;

    @NotNull
    @Getter @Setter private String description;

    @Getter @Setter private String period;
    @Getter @Setter private Integer regeneration;
    @Getter @Setter private Integer hr;
    @Getter @Setter private Integer hrMax;
    @Getter @Setter private Integer cadence;
    @Getter @Setter private Integer power;
    @Getter @Setter private Double ef;
    @Getter @Setter private Double tss;
    @Getter @Setter private Integer elevation;
    @Getter @Setter private String pace;
    @Getter @Setter private Double speed;
    @Getter @Setter private Integer distance;
    @Getter @Setter private String comments;
    @Getter @Setter private String garmin;
    @Getter @Setter private String strava;

    @Column(name="garmin_id")
    @Getter @Setter private Long garminId;

}
