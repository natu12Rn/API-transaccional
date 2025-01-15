package org.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogsUtils {
    private static final Logger logger = LogManager.getLogger();
    private static StringBuilder sb = new StringBuilder();

    public static void logProcesInfo(String message){
        if (sb.length()==0){
            sb.append(message);
            sb.append("\n--------------------\n");
            return;
        }
        sb.append("    ").append(message).append("\n");
    }
    public static void logProcesError(String message){
        sb.append("-").append("[ERROR]").append(message).append("\n");
    }
    public static void logProcesWarn(String message){
        sb.append("-").append("[WARN]").append(message).append("\n");
    }
    public static void logEnvio(){
        sb.append("--------------------");
        logger.info(sb.toString());
        sb.setLength(0);
    }
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
