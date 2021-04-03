package com.z.nativejpablocking.job.service;

import com.z.nativejpablocking.job.dao.JobDAO;
import com.z.nativejpablocking.job.domain.Job;
import com.z.nativejpablocking.job.dto.JobRequest;
import com.z.nativejpablocking.job.dto.JobResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobManagementServiceImpl implements JobManagementService {
    private final JobDAO jobDAO;

    @Override
    public Page<JobResponse> findAll(Pageable pageable) {
        return this.jobDAO.findAll(pageable).map(JobResponse::from);
    }

    @Override
    public JobResponse save(JobRequest jobRequest) {
        return JobResponse.from(this.jobDAO.save(Job.toJob(jobRequest)));
    }
}
