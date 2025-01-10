package org.example.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.fusionauth.jwt.domain.JWT;
import org.example.models.transaccion;
import org.example.dto.transaccionDAO;
import org.example.utils.HttpUtils;
import org.example.utils.JwtUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;


public class transaccionHandler  {
    private transaccionDAO transaccionDAO = new transaccionDAO();
    private HttpUtils HttpUtils = new HttpUtils();

    public void handle(HttpExchange exchange, JWT jwt) throws IOException {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    handleGet(exchange, jwt);
                    break;
                case "POST":
                    if (JwtUtils.valAdmin(jwt)){
                        HttpUtils.enviarRespuesta(exchange, 200, "no tiene autorizacion");
                        break;
                    }
                    handlePost(exchange, jwt);
                    break;
                default:
                    HttpUtils.enviarRespuesta(exchange,405, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.enviarRespuesta(exchange, 500, e.getMessage());
        }
    }

    private void handleGet(HttpExchange exchange, JWT jwt) throws IOException {
        List<transaccion> transaccionList;
        if (JwtUtils.valAdmin(jwt)) {
             transaccionList = transaccionDAO.buscarTodas();
        }else {
             transaccionList = transaccionDAO.buscarTransacciones(jwt.getString("numCuenta"));
        }
        if(transaccionList == null){
            HttpUtils.enviarRespuesta(exchange, 500, "No se encontro las transacciones");
            return;
        }
        JSONArray jsonArray = new JSONArray();
        for (transaccion transaccion : transaccionList) {
            JSONObject obj = new JSONObject();
            obj.put("transaccion_id", transaccion.getTransaccionId());
            obj.put("numero_cuenta", transaccion.getNumcuenta());
            obj.put("tipo_transaccion", transaccion.getTipoTransaccion());
            obj.put("monto", transaccion.getMonto());
            obj.put("cuenta_destino", transaccion.getCuentaDestino());
            obj.put("descripcion", transaccion.getDescripcion());
            obj.put("fecha_transaccion", transaccion.getFechaTransaccion());
            jsonArray.put(obj);
        }
        HttpUtils.enviarRespuesta(exchange, 200, jsonArray.toString());

    }

    private void handlePost(HttpExchange exchange, JWT jwt) throws IOException, SQLException {
        String requestBody = HttpUtils.leerRequestBody(exchange);
        JSONObject obj = new JSONObject(requestBody);

        transaccion transaccion = new transaccion();
        transaccionDAO.getCuenta(transaccion, jwt.getString("numCuenta"));
        transaccion.setTipoTransaccion(obj.getString("tipo_transaccion"));
        transaccion.setMonto(obj.getBigDecimal("monto"));
        transaccion.setDescripcion(obj.getString("descripcion"));
        transaccion.setCuentaDestino(obj.getString("cuenta_destino"));

        switch (transaccion.getTipoTransaccion()){
            case "deposito":
                deposito(transaccion, exchange, jwt.getString("numCuenta"));
                break;
            case "transferencia":
                tranferencia(transaccion, exchange, jwt.getString("numCuenta"));
                break;
            case "retiro":
                retiro(transaccion, exchange, jwt.getString("numCuenta"));
                break;
            default:
                HttpUtils.enviarRespuesta(exchange, 404, "");
        }
    }

    private void deposito(transaccion T, HttpExchange exchange, String numCuenta) throws IOException, SQLException {
        if (T.getMonto().compareTo(BigDecimal.ZERO) < 0){
            HttpUtils.enviarRespuesta(exchange, 400, "Monto negativo");
            return;
        }
        if (!transaccionDAO.Deposito(T, numCuenta)){
            HttpUtils.enviarRespuesta(exchange, 400, "No se puede depositar");
            return;
        }
        JSONObject result = new JSONObject(T);
        HttpUtils.enviarRespuesta(exchange, 200, result.toString());
    }

    private void retiro(transaccion T, HttpExchange exchange, String numCuenta  ) throws IOException, SQLException {
        BigDecimal minimo = new BigDecimal("10000.00");
        if (T.getMonto().compareTo(minimo) < 0){
            HttpUtils.enviarRespuesta(exchange, 400, "Monto por debajo del minimo");
            return;
        }
        if (!transaccionDAO.retirar(T, numCuenta)){
            HttpUtils.enviarRespuesta(exchange, 400, "No se puede retirar");
            return;
        }
        JSONObject result = new JSONObject(T);
        HttpUtils.enviarRespuesta(exchange, 200, result.toString());
    }

    private void tranferencia(transaccion T, HttpExchange exchange, String numCuenta) throws IOException, SQLException {
        if (T.getMonto().compareTo(BigDecimal.ZERO) < 0){
            HttpUtils.enviarRespuesta(exchange, 400, "Monto negativo");
            return;
        }
        if(T.getCuentaDestino().equals(numCuenta)){
            HttpUtils.enviarRespuesta(exchange, 400, "No se puede transferir a ese numero");
            return;
        }
        if (!transaccionDAO.transferencia(T,numCuenta)){
            HttpUtils.enviarRespuesta(exchange, 400, "No se puede transferir");
            return;
        }

        JSONObject result = new JSONObject(T);
        HttpUtils.enviarRespuesta(exchange, 200, result.toString());
    }

}
