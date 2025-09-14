package com.example.prodavnicaracunara.task;

import com.example.prodavnicaracunara.service.NarudzbaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderMonitoringTask {

    private static final Logger logger = LoggerFactory.getLogger(OrderMonitoringTask.class);

    @Autowired
    private NarudzbaService narudzbaService;

    /**
     * Monitoring task that runs every 5 seconds to check and update order statuses
     */
    @Scheduled(fixedRate = 5000) // Run every 5 seconds
    public void monitorOrderStatuses() {
        try {
            logger.debug("Starting order status monitoring task");
            narudzbaService.processOrderStatusUpdates();
            logger.debug("Order status monitoring task completed");
        } catch (Exception e) {
            logger.error("Error occurred during order status monitoring: {}", e.getMessage(), e);
        }
    }

    /**
     * Heartbeat task that runs every minute to log system activity
     */
    @Scheduled(fixedRate = 60000) // Run every minute
    public void heartbeat() {
        logger.info("Order monitoring system is running - heartbeat");
    }
}