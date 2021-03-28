package com.z.nativejpablocking.job.controller;

import com.z.nativejpablocking.job.dto.JobRequest;
import com.z.nativejpablocking.job.dto.JobResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface JobManagementController {
    ResponseEntity<Page<JobResponse>> findAll(Pageable pageable);
    ResponseEntity<JobResponse> save(JobRequest jobRequest);
}
