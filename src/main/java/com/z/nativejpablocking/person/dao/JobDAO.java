package com.z.nativejpablocking.person.dao;

import com.z.nativejpablocking.person.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDAO extends JpaRepository<Job, String> {
}
