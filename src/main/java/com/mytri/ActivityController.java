package com.mytri;

import com.mytri.db.dao.garmin.GarminDao;
import com.mytri.db.model.Activity;
import com.mytri.db.model.User;
import com.mytri.db.repository.ActivityRepository;
import com.mytri.db.repository.UserRepository;
import com.mytri.tools.CsvReader;
import com.mytri.tools.Field;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/api/activity")
public class ActivityController {

    private ActivityRepository activityRepository;
    private UserRepository userRepository;
    private CsvReader csvReader;
    private GarminDao garminDao;

    public ActivityController(ActivityRepository activityRepository, UserRepository userRepository, CsvReader csvReader, GarminDao garminDao) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.csvReader = csvReader;
        this.garminDao = garminDao;
    }

    @GetMapping(path="/fillDataFromExcel")
    public @ResponseBody String fillDataFromExcel() throws IOException {
        User user = userRepository.findByName("lchlebda");
        List<Map<Field, String>> records = csvReader.readRecords("c:/ProgramData/Dzienniczek2018.tsv");
        List<Activity> activities = csvReader.parseRecords(records, user);

        activities.forEach(activity -> {
            activityRepository.save(activity);
        });

        return "saved";
    }

    @GetMapping(path="/{activityId}")
    public @ResponseBody String getActivityFromGarmin(@PathVariable String activityId) {
        return garminDao.getActivity(activityId).getActivityTypeDto().getTypeKey();
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<String> handle(TransactionSystemException exc) {
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
