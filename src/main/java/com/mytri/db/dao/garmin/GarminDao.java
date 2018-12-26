package com.mytri.db.dao.garmin;

import com.mytri.db.model.GarminActivityDto;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class GarminDao {

    private static final String url = "https://connect.garmin.com/modern/proxy/activity-service/activity/";
    String url2 = "https://connect.garmin.com/modern/proxy/activity-service/activity/activityTypes";
    private final RestTemplate restTemplate;

    public GarminDao(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GarminActivityDto getActivity(String id) {
        GarminActivityDto dto = restTemplate.getForObject(url2, GarminActivityDto.class);
        return dto;
    }
}
