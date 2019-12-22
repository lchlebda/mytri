package com.mytri.garmin.dao;

import com.mytri.garmin.model.GarminActivityDto;
import com.mytri.garmin.model.GarminActivityListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
@RequiredArgsConstructor
public class GarminDao {

    private static final String url = "https://connect.garmin.com/modern/proxy";
    private final RestTemplate restTemplate;

    public GarminActivityDto getActivity(String id) {
        GarminActivityDto dto = restTemplate.getForObject(url + "/activity-service/activity/" + id, GarminActivityDto.class);
        return dto;
    }

    public GarminActivityDto[] getActivitiesList(String user, int start, int limit) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url + "/activitylist-service/activities/" + user)
                                                              .queryParam("start", start)
                                                              .queryParam("limit", limit);
        GarminActivityListDto dto = restTemplate.getForObject(uriBuilder.toUriString(), GarminActivityListDto.class);

        return dto.getActivityList();
    }
}
