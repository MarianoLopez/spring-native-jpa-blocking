package com.z.nativejpablocking.job.dao;

import com.z.nativejpablocking.job.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDAO extends JpaRepository<Job, String> {
}
