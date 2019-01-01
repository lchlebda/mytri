package com.mytri.db.enums;

import lombok.Getter;

public enum Sport {
    RUN("Run"),
    SWIM("Swim"),
    CYCLE("Cycle"),
    GYM("Gym"),
    SKI("Ski"),
    SKITOUR("Ski Touring"),
    HIKE("Hike"),
    WALK("Walk"),
    WORKOUT("Workout"),
    COMPETITION("Competition"),
    OTHER("Other");

    @Getter private final String sport;

    Sport(String sport) {
        this.sport = sport;
    }
}
