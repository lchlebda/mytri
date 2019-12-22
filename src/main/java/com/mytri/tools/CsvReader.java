package com.mytri.tools;

import com.mytri.db.enums.Sport;
import com.mytri.db.model.Activity;
import com.mytri.db.model.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.mytri.tools.Field.*;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toList;

@Component
public class CsvReader {

    private static final String SEPARATOR = "\t";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String RUNNING = "Bieganie";
    private static final String SWIMMING = "Pływanie";
    private static final String CYCLING = "Rower";

    public List<Activity> parseRecords(List<Map<Field, String>> records, User user) {
        return records.stream().
                map(record -> toActivity(record, user)).
                collect(toList());
    }

    public List<Map<Field, String>> readRecords(String path) throws IOException {
        return Files.lines(Paths.get(path)).
                skip(1).
                map(line -> line.split(SEPARATOR)).
                filter(record -> record.length >= 17).
                map(this::asFieldsMap).
                collect(toList());
    }

    private Map<Field, String> asFieldsMap(String[] record) {
        Map<Field, String> fieldsMap = new HashMap<>();
        for (int i=1; i<record.length; i++) {
            switch(i) {
                case 1: fieldsMap.put(PERIOD, record[i]); break;
                case 2: fieldsMap.put(DATE, parseNotEmptyField(record[i], DATE, record[2])); break;
                case 4: fieldsMap.put(SPORT, parseNotEmptyField(record[i], SPORT, record[2])); break;
                case 5: fieldsMap.put(DURATION, getNumberOrZero(
                                                    parseNotEmptyField(record[i], DURATION, record[2])));
                                                break;
              //  case 6: fieldsMap.put(REGENERATION, getNumberOrZero(record[i])); break;
                case 6: fieldsMap.put(HR, getNumberOrZero(record[i])); break;
                case 7: fieldsMap.put(HRMAX, getNumberOrZero(record[i])); break;
                case 8: fieldsMap.put(CADENCE, getNumberOrZero(record[i])); break;
                case 9: fieldsMap.put(POWER, getNumberOrZero(record[i])); break;
                case 10: fieldsMap.put(EF, getNumberOrZero(record[i])); break;
              //  case 12: fieldsMap.put(tss, getNumberOrZero(record[i])); break;
                case 11: fieldsMap.put(ELEVATION, getNumberOrZero(record[i])); break;
                case 12: fieldsMap.put(PACE, parsePace(record[i], record[4]));
                         fieldsMap.put(SPEED, parseSpeed(record[i], record[4])); break;
                case 13: fieldsMap.put(DISTANCE, parseDistance(record[i], record[4])); break;
                case 14: fieldsMap.put(DESCRIPTION, parseNotEmptyField(record[i], DESCRIPTION, record[2])); break;
                case 15: fieldsMap.put(COMMENTS, record[i]); break;
                case 16: fieldsMap.put(GARMIN, record[i]); break;
                case 17: fieldsMap.put(STRAVA, record[i]); break;
            }
        }

        return fieldsMap;
    }

    private String parseNotEmptyField(String value, Field field, String date) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("field " + field + " is empty for date " + date);
        }

        return value;
    }

    private String getNumberOrZero(String str) {
        return str.isEmpty() ? "0" : str.replace(',', '.');
    }

    private String parsePace(String speed, String sport) {
        switch (sport) {
            case RUNNING: case SWIMMING: return speed;
            default: return "";
        }
    }

    private String parseSpeed(String speed, String sport) {
        switch (sport) {
            case CYCLING: return getNumberOrZero(speed);
            default: return "0";
        }
    }

    private String parseDistance(String distance, String sport) {
        if (distance.isEmpty()) {
            return "0";
        }
        double dist = Double.valueOf(distance.replace(',', '.'));
        switch (sport) {
            case SWIMMING: return getNumberOrZero(distance);
            default: return Double.toString(dist*1000);
        }
    }

    private Sport mapToEnum(String sport) {
        switch(sport) {
            case "Pływanie": return Sport.SWIM;
            case "Bieganie": return Sport.RUN;
            case "Rower": return Sport.CYCLE;
            case "Góry": return Sport.HIKE;
            case "Siłownia": return Sport.GYM;
            case "Narty": return Sport.SKI;
            default: return Sport.OTHER;
        }
    }

    private final Map<Field, BiConsumer<String, Activity>> mapper = Map.ofEntries(
            entry(PERIOD, (period, activity) -> activity.setPeriod(period)),
            entry(DATE, (date, activity) -> activity.setDate(LocalDateTime.parse(date, FORMATTER))),
            entry(SPORT, (sport, activity) -> activity.setSport(mapToEnum(sport))),
            entry(DURATION, (duration, activity) -> activity.setDuration(Integer.valueOf(duration))),
            entry(REGENERATION, (regeneration, activity) -> activity.setRegeneration(Integer.valueOf(regeneration))),
            entry(HR, (hr, activity) -> activity.setHr(Integer.valueOf(hr))),
            entry(HRMAX, (hrmax, activity) -> activity.setHrMax(Integer.valueOf(hrmax))),
            entry(CADENCE, (cadence, activity) -> activity.setCadence(Integer.valueOf(cadence))),
            entry(POWER, (power, activity) -> activity.setPower(Integer.valueOf(power))),
            entry(EF, (ef, activity) -> activity.setEf(Double.valueOf(ef))),
            entry(TSS, (tss, activity) -> activity.setTss(Double.valueOf(tss))),
            entry(ELEVATION, (elevation, activity) -> activity.setElevation(Integer.valueOf(elevation))),
            entry(PACE, (pace, activity) -> activity.setPace(pace)),
                entry(SPEED, (speed, activity) -> activity.setSpeed(Double.valueOf(speed))),
            entry(DISTANCE, (distance, activity) -> activity.setDistance(Integer.valueOf(distance))),
            entry(DESCRIPTION, (description, activity) -> activity.setDescription(description)),
            entry(COMMENTS, (comments, activity) -> activity.setComments(comments)),
            entry(GARMIN, (garmin, activity) -> activity.setGarmin(garmin)),
            entry(STRAVA, (strava, activity) -> activity.setStrava(strava))
    );

    private Activity toActivity(Map<Field, String> record, User user) {
        Activity activity = new Activity();
        activity.setUser(user);
        record.forEach((field, value) -> mapper.get(field).accept(value, activity));

        return activity;
    }

}