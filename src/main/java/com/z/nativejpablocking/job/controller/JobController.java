package com.z.nativejpablocking.job.controller;

import com.z.nativejpablocking.job.dto.JobRequest;
import com.z.nativejpablocking.job.dto.JobResponse;
import com.z.nativejpablocking.job.service.JobManagementService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.z.nativejpablocking.job.controller.JobController.BASE_URL;

@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_URL)
public class JobController implements JobManagementController {
    static final String BASE_URL = "/api/v1/job";
    private final JobManagementService jobManagementService;

    @GetMapping
    @Override
    public ResponseEntity<Page<JobResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(this.jobManagementService.findAll(pageable));
    }

    @SneakyThrows
    @PostMapping
    @Override
    public ResponseEntity<JobResponse> save(@RequestBody @Validated JobRequest jobRequest) {
        var job = this.jobManagementService.save(jobRequest);
        return ResponseEntity
                .created(URI.create(BASE_URL + "/" + URLEncoder.encode(job.getName(), StandardCharsets.UTF_8)))
                .body(job);
    }
}
