package com.mytri.services;

import com.mytri.db.model.Activity;
import com.mytri.db.repository.ActivityRepository;
import com.mytri.garmin.dao.GarminDao;
import com.mytri.garmin.model.GarminActivityDto;
import com.mytri.garmin.service.GarminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private static final int LIMIT = 3;

    private final ActivityRepository activityRepository;
    private final GarminService garminService;
    private final GarminDao garminDao;

    public GarminActivityDto[] updateActivitiesForUser(String user) {
        Activity activity = activityRepository.findFirstByGarminIsNotNullOrderByDateDesc();
        LocalDateTime lastActivityDate = activity.getDate();
        GarminActivityDto[] activities = garminService.getActivityFromStartToLimit(user, 1, LIMIT);

        return activities;
        // wziąć czas z activity, pobrać ostatnie 3 aktywnosci z garmina i wziąć tylko te, które są młodsze od tej z activity - pobrać z nich idki. Jeśli wszystkie są
        // młodsze, to znaczy, że może być ich więcej, więc pobrać kolejne 3 itd
    }

//    private GarminActivityDto[] getNextActivities(int start) {
//        garminService.getActivityFromStartToLimit(user, 1, LIMIT);
//    }
}
