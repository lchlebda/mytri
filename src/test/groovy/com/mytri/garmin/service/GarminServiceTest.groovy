package com.mytri.garmin.service

import com.mytri.db.enums.Sport
import com.mytri.garmin.dao.GarminDao
import com.mytri.garmin.enums.ActivityType
import com.mytri.garmin.model.GarminActivityDto
import com.mytri.garmin.service.GarminService
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.Month

class GarminServiceTest extends Specification {

    def garminDao = Mock(GarminDao)
    def garminActivityDto = Mock(GarminActivityDto)
    def activityTypeDto = Mock(GarminActivityDto.ActivityTypeDto)

    def garminService = new GarminService(garminDao)

    def "run pace should be calculated from speed"(double speed, String pace) {
        setup:
            setUpGarminActivityDto(ActivityType.RUNNING)
            garminActivityDto.getAverageSpeed() >> speed

        expect:
            garminService.getActivity("1111").getPace() == pace

        where:
            speed || pace
            2.86  || "5:50"
            4.02  || "4:09"
            4.53  || "3:41"
            3.334 || "5:00"
    }

    def "swim pace should be calculated from speed"(double speed, String pace) {
        setup:
            setUpGarminActivityDto(ActivityType.SWIMMING)
            garminActivityDto.getAverageSpeed() >> speed

        expect:
            garminService.getActivity("1111").getPace() == pace

        where:
            speed  ||  pace
            0.715  || "2:20"
            0.992  || "1:41"
            0.794  || "2:06"
            0.834  || "2:00"
    }

    def "speed should be calculated correctly"(double givenSpeed, double expectedSpeed) {
        setup:
            setUpGarminActivityDto(ActivityType.CYCLING)
            garminActivityDto.getAverageSpeed() >> givenSpeed

        expect:
            garminService.getActivity("1111").getSpeed() == expectedSpeed

        where:
            givenSpeed  || expectedSpeed
            8.325       || 30
            8.498       || 30.6
            8.306       || 29.9
            8.438       || 30.4
            2.918       || 10.5
    }

    def "duration should be rounded to multiply of 5"(int givenDuration, int expectedDuration) {
        setup:
            setUpGarminActivityDto(ActivityType.RUNNING)
            garminActivityDto.getDuration() >> givenDuration

        expect:
            garminService.getActivity("1111").getDuration() == expectedDuration

        where:
            givenDuration || expectedDuration
                5911      ||        100
                7148      ||        120
                3846      ||        65
                4862      ||        85
    }

    def "garmin service should convert cycle object from garmin endpoint to my database dto"() {
        given:
            setUpGarminActivityDto(ActivityType.CYCLING)
            garminActivityDto.getAverageSpeed() >> 8.325
            garminActivityDto.getAverageHR() >> 145
            garminActivityDto.getDuration() >> 1417
            garminActivityDto.getElevationGain() >> 27
            garminActivityDto.getMaxHR() >> 150
            garminActivityDto.getStartTimeLocal() >> LocalDateTime.parse("2018-12-25T12:00:00")
            garminActivityDto.getDistance() >> 4052
            garminActivityDto.getBikeCadence() >> 85
            garminActivityDto.getNormPower() >> 238
            garminActivityDto.getTss() >> 80.3

        when:
            def activity = garminService.getActivity("1111")

        then:
            activity.getSpeed() == 30
            activity.getDuration() == 25
            activity.getDate().getMonth() == Month.DECEMBER
            activity.getDate().getDayOfMonth() == 25
            activity.getDate().getYear() == 2018
            activity.getDistance() == 4052
            activity.getElevation() == 27
            activity.getHr() == 145
            activity.getHrMax() == 150
            activity.getTss() == 80.3
            activity.getPower() == 238
            activity.getCadence() == 85
            activity.getSport() == Sport.CYCLE
    }

    def setUpGarminActivityDto(activityType) {
        activityTypeDto.getTypeKey() >> activityType
        garminActivityDto.getActivityType() >> activityTypeDto
        garminDao.getActivity(_) >> garminActivityDto
    }
}
