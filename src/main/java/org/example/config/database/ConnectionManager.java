package org.example.config.database;

import org.example.utils.LogsUtils;
import snaq.db.ConnectionPool;

import java.sql.Connection;
import java.util.Properties;

public class ConnectionManager {
    private static ConnectionManager instance;
    private ConnectionPool pool;

    private ConnectionManager(){
        InitializedPool();
    }

    public static ConnectionManager getInstance(){
        LogsUtils.logInfo("Instanciando ConnectionManager");
        if(instance == null){
            synchronized (ConnectionManager.class){
                if(instance == null){
                    instance = new ConnectionManager();
                }
            }
        }
        LogsUtils.logInfo("ConnectionManager instanciada");
        return instance;
    }

    private void InitializedPool(){
        try{
            LogsUtils.logInfo("Inicializando pool de conexiones");
            Properties props = new Properties();
            props.setProperty("user", DatabaseConfig.getProperty("db.user"));
            props.setProperty("password", DatabaseConfig.getProperty("db.password"));

            // Agregamos las propiedades específicas de PostgreSQL
            props.setProperty("ssl", DatabaseConfig.getProperty("db.ssl"));
            props.setProperty("tcpKeepAlive", DatabaseConfig.getProperty("db.tcpKeepAlive"));
            props.setProperty("binaryTransfer", DatabaseConfig.getProperty("db.binaryTransfer"));

            // Creamos el pool con las propiedades
            pool = new ConnectionPool(
                    DatabaseConfig.getProperty("db.pool.name"),
                    DatabaseConfig.getIntProperty("db.pool.min"),
                    DatabaseConfig.getIntProperty("db.pool.max"),
                    DatabaseConfig.getIntProperty("db.pool.maxsize"),
                    DatabaseConfig.getLongProperty("db.pool.idletimeout"),
                    DatabaseConfig.getProperty("db.url"),
                    props    // Pasamos las propiedades directamente al constructor
            );

            pool.init();
            LogsUtils.logInfo("Pool de conexiones inicializado");
        }catch (Exception e){
            LogsUtils.logError(e.getMessage());
        }
    }
    public Connection getConnection() throws Exception {
        try {
            Connection conn = pool.getConnection();
            // Configurar el esquema si está especificado
            String schema = DatabaseConfig.getProperty("db.schema");
            if (schema != null && !schema.isEmpty()) {
                conn.setSchema(schema);
            }
            LogsUtils.logProcesInfo("Conexion obtenida");
            return conn;
        } catch (Exception e) {
            LogsUtils.logProcesError("Error al obtener la conexion --> " + e.getMessage());
            LogsUtils.logEnvio();
            throw e;
        }
    }

    public void closePool() {
        if (pool != null) {
            pool.release();
        }
    }
}
