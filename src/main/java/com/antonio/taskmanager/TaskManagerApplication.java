package com.antonio.taskmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskManagerApplication {

	private static Logger logger = LoggerFactory.getLogger(TaskManagerApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Task Manager Application...");
		SpringApplication.run(TaskManagerApplication.class, args);
		logger.info("Task Manager Application started successfully.");
	}
}
