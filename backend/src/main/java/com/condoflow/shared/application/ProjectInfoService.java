package com.condoflow.shared.application;

import org.springframework.stereotype.Service;

@Service
public class ProjectInfoService {

    public String projectName() {
        return "condoflow-backend";
    }

    public String backendStage() {
        return "SPRING_BOOT_BASE";
    }
}