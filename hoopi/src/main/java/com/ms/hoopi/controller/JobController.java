package com.ms.hoopi.controller;

import com.ms.hoopi.model.dto.JobPostingDto;
import com.ms.hoopi.model.entity.Company;
import com.ms.hoopi.service.JobService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("hoopi")
public class JobController {
    @Autowired
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    @PostMapping("/job")
    public ResponseEntity<String> insertPostJob(@RequestBody JobPostingDto jobPosting) {
        return jobService.insertJob(jobPosting);
    }
    @GetMapping("/job")
    public ResponseEntity<Map<String, Object>> getJob(@RequestBody HttpServletRequest request, String userId, String search) {
        Map map = jobService.getJob(request, search);
        return ResponseEntity.ok(map);
    }
}
