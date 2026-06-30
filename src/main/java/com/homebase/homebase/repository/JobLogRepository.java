package com.homebase.homebase.repository;


import com.homebase.homebase.model.JobLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobLogRepository extends JpaRepository<JobLog, Long> {

    List<JobLog> findByJobName(String jobName);

    List<JobLog> findByStatus(String status);

    List<JobLog> findByJobNameAndStatus(String jobName, String status);

    List<JobLog> findByJobNameOrderByStartedAtDesc(String jobName);
}
