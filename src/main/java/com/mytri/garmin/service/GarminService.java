package com.mytri.garmin.service;

import com.mytri.db.enums.Sport;
import com.mytri.db.model.Activity;
import com.mytri.garmin.dao.GarminDao;
import com.mytri.garmin.enums.ActivityType;
import com.mytri.garmin.model.GarminActivityDto;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class GarminService {

    private static final double MAGIC_GARMIN_FACTOR = 3.6;
    private static final int RUN_DIVISION_FACTOR = 3600;  // run pace is for 1km, that's why is 3600secs
    private static final int SWIM_DIVISION_FACTOR = 360;  // swim pace is for 100m, that's why is 360secs

    private final GarminDao garminDao;

    public GarminService(GarminDao garminDao) {
        this.garminDao = garminDao;
    }

    public Activity getActivity(String id) {
        GarminActivityDto garminActivityDto = garminDao.getActivity(id);
        return mapToActivity(garminActivityDto);
    }

    private Activity mapToActivity(GarminActivityDto dto) {
        Activity activity = new Activity();

        activity.setDistance((int)dto.getSummaryDTO().getDistance());
        activity.setCadence(dto.getSummaryDTO().getAverageBikeCadence());
        activity.setHrMax(dto.getSummaryDTO().getMaxHR());
        activity.setHr(dto.getSummaryDTO().getAverageHR());
        activity.setDate(dto.getSummaryDTO().getStartTimeLocal());
        activity.setElevation(dto.getSummaryDTO().getElevationGain());
        activity.setPower(dto.getSummaryDTO().getNormalizedPower());
        activity.setTss(dto.getSummaryDTO().getTrainingStressScore());

        double speed = dto.getSummaryDTO().getAverageSpeed() * MAGIC_GARMIN_FACTOR;
        activity.setSpeed((double)Math.round(speed*10)/10);

        Sport sport = mapToSport(dto.getActivityTypeDTO().getTypeKey());
        activity.setSport(sport);

        String pace = mapToPace(activity.getSport(), speed);
        activity.setPace(pace);

        int duration = calculateDuration(dto.getSummaryDTO().getDuration());
        activity.setDuration(duration);

        return activity;
    }

    private Sport mapToSport(ActivityType activityType) {
        switch(activityType) {
            case CYCLING: return Sport.CYCLE;
            case RUNNING: return Sport.RUN;
            case SWIMMING: return Sport.SWIM;
            case HIKING: return Sport.HIKE;
            case SKIING: return Sport.SKI;
            case WALKING: return Sport.WALK;
            case SKITOURING: return Sport.SKITOUR;
            case MULTI_SPORT: return Sport.COMPETITION;
            case ROAD_BIKING: return Sport.CYCLE;
            case MOUNTAINEERING: return Sport.HIKE;
            case MOUNTAIN_BIKING: return Sport.CYCLE;
            case OPEN_WATER_SWIMMING: return Sport.SWIM;
            case OTHER: return Sport.OTHER;
            default: return Sport.OTHER;
        }
    }

    private String mapToPace(Sport sport, double speed) {
        switch(sport) {
            case RUN: return calculatePace(speed, RUN_DIVISION_FACTOR);
            case SWIM: return calculatePace(speed, SWIM_DIVISION_FACTOR);
            default: return "";
        }
    }

    private String calculatePace(double speed, int divisionFactor) {
        int seconds = (int) Math.ceil(divisionFactor/speed);
        int minutesPace = seconds/60;
        int secondsPace = seconds % 60;

        return Integer.toString(minutesPace) + ":" + (secondsPace < 10 ? "0" : "") + Integer.toString(secondsPace);
    }

    private int calculateDuration(int duration) {
        int minutes = (int)TimeUnit.SECONDS.toMinutes(duration);
        return ((minutes / 5) + 1) * 5; // rounds to first multiple of 5 higher than duration
    }
}
