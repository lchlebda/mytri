package com.mytri.garmin.model;

import com.mytri.garmin.enums.ActivityType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
public class GarminActivityDto {

    @Getter @Setter private long activityId;
    @Getter @Setter private ActivityTypeDto activityTypeDTO;
    @Getter @Setter private SummaryDto summaryDTO;

    @Data
    public static class ActivityTypeDto {
        @Getter @Setter private ActivityType typeKey;
    }

    @Data
    public static class SummaryDto {
        @Getter @Setter private LocalDate startTimeLocal;
        @Getter @Setter private int duration;
        @Getter @Setter private int movingDuration;
        @Getter @Setter private double distance;
        @Getter @Setter private int elevationGain;
        @Getter @Setter private double averageSpeed;
        @Getter @Setter private int averageHR;
        @Getter @Setter private int maxHR;
        @Getter @Setter private int averageBikeCadence;
        @Getter @Setter private int normalizedPower;
        @Getter @Setter private double trainingStressScore;
    }
}
