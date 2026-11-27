package com.alexkrug.backend;

import org.springframework.boot.SpringApplication;

public class TestsTaskTrackerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.from(TaskTrackerBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}