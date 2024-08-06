package com.ms.hoopi.service;

import com.ms.hoopi.model.dto.JobPostingDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public interface JobService {
    public ResponseEntity<String> insertJob(JobPostingDto jobPosting);

    Map<String, Object> getJob(HttpServletRequest request, String search);
}
