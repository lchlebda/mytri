package com.mytri.garmin.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class GarminActivityListDto {
    @Getter @Setter private GarminActivityDto[] activityList;
}
