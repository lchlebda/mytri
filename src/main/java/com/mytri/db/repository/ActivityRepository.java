package com.mytri.db.repository;

import com.mytri.db.model.Activity;
import org.springframework.data.repository.CrudRepository;

public interface ActivityRepository extends CrudRepository<Activity, Long> {
    Activity findFirstByGarminIsNotNullOrderByDateDesc();
}
