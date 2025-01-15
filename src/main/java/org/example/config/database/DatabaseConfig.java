package org.example.config.database;

import org.example.utils.LogsUtils;

import java.io.FileInputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static Properties properties = new Properties();
    private static final String CONFIG_FILE = "src/main/resources/dbConfig.properties";

    static {
        loadProperties();
        try{
            LogsUtils.logInfo("Cargando driver de base de datos");
            Class.forName(properties.getProperty("db.driver"));
            LogsUtils.logInfo("Driver cargado --> " + properties.getProperty("db.driver"));
        }catch (ClassNotFoundException e){
            LogsUtils.logError(e.getMessage());
        }
    }

    private static void loadProperties() {
        try(FileInputStream flis = new FileInputStream(CONFIG_FILE)){
            LogsUtils.logInfo("Cargando archivo de configuracion de base de datos");
            properties.load(flis);
            LogsUtils.logInfo("Archivo de configuracion de base de datos cargado");
        }catch (Exception e){
            LogsUtils.logError(e.getMessage());
        }
    }
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Propiedad no encontrada: " + key);
        }
        return value;
    }

    public static int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public static long getLongProperty(String key) {
        return Long.parseLong(getProperty(key));
    }

    public static boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
}
