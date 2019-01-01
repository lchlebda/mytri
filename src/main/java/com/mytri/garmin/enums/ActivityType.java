package com.mytri.garmin.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ActivityType {

    RUNNING("running"),
    CYCLING("cycling"),
    SWIMMING("swimming"),
    OPEN_WATER_SWIMMING("open_water_swimming"),
    ROAD_BIKING("road_biking"),
    HIKING("hiking"),
    MOUNTAIN_BIKING("mountain_biking"),
    WALKING("walking"),
    MOUNTAINEERING("mountaineering"),
    MULTI_SPORT("multi_sport"),
    SKIING("resort_skiing_snowboarding"),
    SKITOURING("backcountry_skiing_snowboarding"),
    OTHER("other");

    @Getter @JsonValue private String type;

    ActivityType(String type) {
        this.type = type;
    }

}
