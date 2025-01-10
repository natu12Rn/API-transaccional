package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.handler.handlerProtected;
import org.example.handler.userHandler;


import java.io.IOException;
import java.net.InetSocketAddress;


public class app {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create( new InetSocketAddress(8000),0) ;

        server.createContext("/api/transaccion", new handlerProtected());
        server.createContext("/api/login", new userHandler());
        server.createContext("/api/Registrar", new userHandler());
        server.createContext("/api/cuenta", new handlerProtected());


        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port http://localhost:8000");
    }
}