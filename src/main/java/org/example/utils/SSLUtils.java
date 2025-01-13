package org.example.utils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.Date;

public class SSLUtils {
    private static final String KEYSTORE_FILE =  "keystore.jks";
    private static final String KEYSTORE_PASSWORD = "Copito12324";

    public static SSLContext getSSLContext() throws Exception {
        generarKeyStore();

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(KEYSTORE_FILE), KEYSTORE_PASSWORD.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, KEYSTORE_PASSWORD.toCharArray());

        // Crear y configurar el contexto SSL
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);

        return sslContext;
    }

    private static void generarKeyStore() {
        File keyStoreFile = new File(KEYSTORE_FILE);

        // Cambiado: Solo generar si NO existe
        if (keyStoreFile.exists()) {
            System.out.println("KeyStore ya existe en: " + keyStoreFile.getAbsolutePath());
            verificarKeyStore();  // Verificar el KeyStore existente
            return;
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "keytool",
                    "-genkeypair",
                    "-keyalg", "RSA",
                    "-keysize", "2048",
                    "-validity", "365",
                    "-alias", "servidor",
                    "-keystore", KEYSTORE_FILE,
                    "-storepass", KEYSTORE_PASSWORD,
                    "-keypass", KEYSTORE_PASSWORD,
                    "-dname", "CN=localhost, OU=Development, O=TuOrganizacion, L=Ciudad, S=Estado, C=ES"
            );

            Process proceso = processBuilder.start();
            int exitCode = proceso.waitFor();

            if (exitCode == 0) {
                System.out.println("KeyStore generado exitosamente en: " + keyStoreFile.getAbsolutePath());
                verificarKeyStore();  // Verificar el KeyStore recién creado
            } else {
                System.err.println("Error al generar KeyStore. Código de salida: " + exitCode);
            }
        } catch (Exception e) {
            System.err.println("Error al generar el KeyStore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void verificarKeyStore() {
        try {
            // Intentar listar el contenido del KeyStore
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "keytool",
                    "-list",
                    "-keystore", KEYSTORE_FILE,
                    "-storepass", KEYSTORE_PASSWORD
            );

            Process proceso = processBuilder.start();

            // Leer la salida del proceso
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(proceso.getInputStream()))) {
                String linea;
                System.out.println("\nContenido del KeyStore:");
                while ((linea = reader.readLine()) != null) {
                    System.out.println(linea);
                }
            }

            int exitCode = proceso.waitFor();
            if (exitCode == 0) {
                System.out.println("\nVerificación del KeyStore exitosa!");
            } else {
                System.err.println("\nError al verificar el KeyStore. Código de salida: " + exitCode);
            }

            // Verificar el archivo físicamente
            File keyStoreFile = new File(KEYSTORE_FILE);
            System.out.println("\nInformación del archivo KeyStore:");
            System.out.println("Ruta: " + keyStoreFile.getAbsolutePath());
            System.out.println("Tamaño: " + keyStoreFile.length() + " bytes");
            System.out.println("Última modificación: " + new Date(keyStoreFile.lastModified()));
            System.out.println("¿Existe? " + keyStoreFile.exists());
            System.out.println("¿Se puede leer? " + keyStoreFile.canRead());

        } catch (Exception e) {
            System.err.println("Error al verificar el KeyStore: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
