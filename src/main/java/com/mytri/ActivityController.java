package com.mytri;

import com.mytri.db.model.Activity;
import com.mytri.db.model.User;
import com.mytri.db.repository.ActivityRepository;
import com.mytri.db.repository.UserRepository;
import com.mytri.garmin.service.GarminService;
import com.mytri.tools.CsvReader;
import com.mytri.tools.Field;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/api")
public class ActivityController {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final CsvReader csvReader;
    private final GarminService garminService;

    public ActivityController(ActivityRepository activityRepository, UserRepository userRepository, CsvReader csvReader, GarminService garminService) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.csvReader = csvReader;
        this.garminService = garminService;
    }

    @GetMapping(path="/fillDataFromExcel")
    public @ResponseBody String fillDataFromExcel() throws IOException {
        User user = userRepository.findByName("lchlebda");
        List<Map<Field, String>> records = csvReader.readRecords("c:/ProgramData/Dzienniczek2016.tsv");
        List<Activity> activities = csvReader.parseRecords(records, user);

        activities.forEach(activity -> {
            activityRepository.save(activity);
        });

        return "saved";
    }

    @GetMapping(path="/activity/{activityId}")
    public @ResponseBody Activity getActivity(@PathVariable Long activityId) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(
                () -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        return activity;
    }

    @DeleteMapping(path="/activity/{activityId}")
    public @ResponseBody void deleteActivity(@PathVariable long activityId) {
        activityRepository.deleteById(activityId);
    }

    @GetMapping(path="/garmin/activity/{activityId}")
    public @ResponseBody Activity getActivityFromGarmin(@PathVariable String activityId) {
        Activity activity = garminService.getActivity(activityId);
        User user = userRepository.findByName("lchlebda");
        activity.setUser(user);
        activity.setDescription("");
        activityRepository.save(activity);

        return activity;
    }

//    @GetMapping(path="/garmin/user/{user}/activities")
//    public @ResponseBody List<Long> getActivitiesFromGarmin(@PathVariable String user,
//                                                               @RequestParam int start,
//                                                               @RequestParam int limit) {
//
//        return Arrays.stream(garminDao.getActivitiesList(user, start, limit))
//                     .map(GarminActivityDto::getActivityId)
//                     .collect(toList());
//    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<String> handle(TransactionSystemException exc) {
       return new ResponseEntity<>(exc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handle(HttpClientErrorException exc) {
        return new ResponseEntity<>(exc.getMessage(), exc.getStatusCode());
    }

}
