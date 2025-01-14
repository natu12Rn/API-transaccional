package org.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogsUtils {
    private static final Logger logger = LogManager.getLogger();

    public static void logInfo(String message) {
        logger.info(message);
    }
    public static void logError(String message) {
        logger.error(message);
    }
    public static void logWarn(String message) {
        logger.warn(message);
    }
    public static void logDebug(String message) {
        logger.debug(message);
    }
}
