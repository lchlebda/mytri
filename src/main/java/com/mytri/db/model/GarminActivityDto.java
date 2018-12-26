package com.mytri.db.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class GarminActivityDto {

    @Getter @Setter private ActivityTypeDto activityTypeDto;

    @Data
    public static class ActivityTypeDto {
        @Getter @Setter private String typeKey;
    }
}
