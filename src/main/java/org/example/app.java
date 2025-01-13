package org.example;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import org.example.handler.handlerProtected;
import org.example.handler.userHandler;
import org.example.utils.SSLUtils;


import java.net.InetSocketAddress;


public class app {
    public static void main(String[] args) throws Exception {
        HttpsServer server = HttpsServer.create(new InetSocketAddress(8080), 0);

        server.setHttpsConfigurator(new HttpsConfigurator(SSLUtils.getSSLContext()));

        server.createContext("/api/transaccion", new handlerProtected());
        server.createContext("/api/login", new userHandler());
        server.createContext("/api/Registrar", new userHandler());
        server.createContext("/api/cuenta", new handlerProtected());


        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port https://localhost:8080");
    }
}