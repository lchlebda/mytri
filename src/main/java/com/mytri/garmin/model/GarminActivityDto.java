package com.mytri.garmin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mytri.garmin.enums.ActivityType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class GarminActivityDto {

    @Getter @Setter private long activityId;
    @Getter @Setter private ActivityTypeDto activityType;

    @Getter @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTimeLocal;

    @Getter @Setter private int duration;
    @Getter @Setter private int movingDuration;
    @Getter @Setter private double distance;
    @Getter @Setter private int elevationGain;
    @Getter @Setter private double averageSpeed;
    @Getter @Setter private int averageHR;
    @Getter @Setter private int maxHR;
    @Getter @Setter private int bikeCadence;
    @Getter @Setter private int normPower;
    @Getter @Setter private double tss;

    @JsonCreator
    public GarminActivityDto(@JsonProperty("summaryDTO") SummaryDto summaryDTO,
                             @JsonProperty("activityTypeDTO") ActivityTypeDto activityType,
                             @JsonProperty("averageBikingCadenceInRevPerMinute") int bikeCadence,
                             @JsonProperty("trainingStressScore") double tss) {

        if (summaryDTO == null || activityType == null) {
            this.bikeCadence = bikeCadence;
            this.tss = tss;

            return;
        }

        this.startTimeLocal = summaryDTO.getStartTimeLocal();
        this.duration = summaryDTO.getDuration();
        this.movingDuration = summaryDTO.getMovingDuration();
        this.distance = summaryDTO.getDistance();
        this.elevationGain = summaryDTO.getElevationGain();
        this.averageSpeed = summaryDTO.getAverageSpeed();
        this.averageHR = summaryDTO.getAverageHR();
        this.maxHR = summaryDTO.getMaxHR();
        this.bikeCadence = summaryDTO.getAverageBikeCadence();
        this.normPower = summaryDTO.getNormalizedPower();
        this.tss = summaryDTO.getTrainingStressScore();
        this.activityType = activityType;
    }

    @Data
    public static class ActivityTypeDto {
        @Getter @Setter private ActivityType typeKey;
    }

    @Data
    private static class SummaryDto {
        @Getter @Setter private LocalDateTime startTimeLocal;
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
