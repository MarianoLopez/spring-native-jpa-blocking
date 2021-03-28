package com.z.nativejpablocking.job.service;

import com.z.nativejpablocking.job.dto.JobRequest;
import com.z.nativejpablocking.job.dto.JobResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobManagementService {
    Page<JobResponse> findAll(Pageable pageable);
    JobResponse save(JobRequest jobRequest);
}
