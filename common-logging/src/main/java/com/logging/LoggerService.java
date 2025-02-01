package com.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerService {
    private static final Logger logger = LoggerFactory.getLogger(LoggerService.class);
    private static LoggerService instance;

    private LoggerService() {}

    public static LoggerService getInstance() {
        if (instance == null) {
            synchronized (LoggerService.class) {
                if (instance == null) {
                    instance = new LoggerService();
                }
            }
        }
        return instance;
    }

    public void info(String message) {
        logger.info(message);
    }

    public void error(String message, Exception e) {
        logger.error(message, e);
    }
}
