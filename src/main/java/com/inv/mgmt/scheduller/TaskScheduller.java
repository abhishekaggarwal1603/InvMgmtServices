package com.inv.mgmt.scheduller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.inv.mgmt.controller.HomeController;
import com.inv.mgmt.repo.ProductMasterRepo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;


@Component
public class TaskScheduller {
	
	@Autowired
	HomeController homeController;
	
	
	    private static final Logger logger = LoggerFactory.getLogger(TaskScheduller.class);
	    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	//    @Scheduled(fixedRate = 600000)
	    @Scheduled(fixedRate = 10000)
	    public void scheduleTaskWithFixedRate() {
	//    logger.info("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) );
	    homeController.calculateReorderNotifications();
	    homeController.calculateCount();
	    homeController.calculateCheckOutTypes();
	    homeController.calculateCheckInTypes();
	    homeController.calculateLineGraphCheckOutData();
	    homeController.calculateLineGraphCheckInData();
	    
	    }

	    public void scheduleTaskWithFixedDelay() {}

	    public void scheduleTaskWithInitialDelay() {}

	    public void scheduleTaskWithCronExpression() {}
	}