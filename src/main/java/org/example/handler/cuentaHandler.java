package org.example.handler;

import com.sun.net.httpserver.HttpExchange;
import io.fusionauth.jwt.domain.JWT;
import org.example.dto.cuentaDAO;
import org.example.models.cuenta;
import org.example.models.users;
import org.example.utils.HttpUtils;
import org.example.utils.JwtUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


public class cuentaHandler {
    private HttpUtils httpUtils = new HttpUtils();
    private static cuentaDAO cuentaDao = new cuentaDAO();

    public void handle(HttpExchange exchange, JWT jwt) throws IOException {
        try{
            if(!exchange.getRequestMethod().equals("GET")){
                httpUtils.enviarRespuesta(exchange,405, "metodo no existente");
                return;
            }
            if (JwtUtils.valAdmin(jwt)){
                handlerGetAdmin(exchange);
            }else{
                handlerGetUser(exchange, jwt);
            }

        }catch (Exception e){
            httpUtils.enviarRespuesta(exchange, 400, e.getMessage());
        }
    }

    public void handlerGetUser(HttpExchange exchange, JWT jwt) throws IOException {
        cuenta cuenta = cuentaDAO.CuentaPersonal(jwt);
        if (cuenta == null){
            httpUtils.enviarRespuesta(exchange, 400, "No se encontro la cuenta");
            return;
        }
        JSONObject obj = new JSONObject(cuenta);
        httpUtils.enviarRespuesta(exchange, 200, obj.toString());
    }

    public void handlerGetAdmin (HttpExchange exchange) throws IOException {
        List<users> listUser = cuentaDAO.listCuentas();
        if (listUser == null){
            httpUtils.enviarRespuesta(exchange, 400, "No existente");
        }
        JSONArray jsonArray = new JSONArray();
        for (users user : listUser){
            JSONObject obj = new JSONObject();
            obj.put("name", user.getName());
            obj.put("email", user.getEmail());
            obj.put("login", user.getLogin());
            obj.put("activo", user.getActivo());
            obj.put("numero_cuenta",user.getCuenta().getNumCuenta());
            obj.put("tipo_cuenta", user.getCuenta().getTipoCuenta());
            jsonArray.put(obj);
        }
        httpUtils.enviarRespuesta(exchange, 200, jsonArray.toString());
    }

}
