package org.example.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.fusionauth.jwt.domain.JWT;
import org.example.utils.HttpUtils;
import org.example.utils.JwtUtils;

import java.io.IOException;

public class handlerProtected implements HttpHandler {
    private HttpUtils httpUtils = new HttpUtils();
    private cuentaHandler cuentaHandler = new cuentaHandler();
    private transaccionHandler transaccionHandler = new transaccionHandler();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String token = httpUtils.getTokenFromHeader(exchange);

        if (token == null) {
            httpUtils.enviarRespuesta(exchange,401,"token no proporcionado");
            return;
        }

        JWT jwt = JwtUtils.validacionToken(token);

        if (jwt == null) {
            httpUtils.enviarRespuesta(exchange,401,"token no validado");
            return;
        }

        String [] path = exchange.getRequestURI().getPath().split("/");
        switch (path[2]){
            case "cuenta":
                cuentaHandler.handle(exchange, jwt);
                break;
            case "transaccion":
                transaccionHandler.handle(exchange, jwt);
                break;
            default:
                httpUtils.enviarRespuesta(exchange,401,"Direccion no valida");
        }
    }
}
