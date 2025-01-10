package org.example.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;

public class HttpUtils {

    public void enviarRespuesta(HttpExchange exchange, int statusCode, String response) throws IOException{
        byte[] responseBytes = response.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
    public static String leerRequestBody (HttpExchange exchange) throws IOException {
        InputStream isr = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "UTF-8"));
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        return requestBody.toString();
    }

    public String getTokenFromHeader (HttpExchange exchange){
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if(authHeader != null){
            return authHeader.substring(7);
        }
        return null;
    }
}
