package org.example.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.dto.cuentaDAO;
import org.example.dto.userDAO;
import org.example.models.users;
import org.example.utils.HttpUtils;
import org.example.utils.JwtUtils;
import org.example.utils.Utils;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

public class userHandler implements HttpHandler {
    private final userDAO dao = new userDAO();
    private static final cuentaDAO cuentaDao = new cuentaDAO();
    private final HttpUtils httpUtils = new HttpUtils();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try{
            if (!exchange.getRequestMethod().equals("POST")) {
                httpUtils.enviarRespuesta(exchange,405, "metodo no existente");
                return;
            }
            String [] path = exchange.getRequestURI().getPath().split("/");
            switch (path[2]){
                case "login":
                    handlerPostLogin(exchange);
                    break;
                case "Registrar":
                    handlerPostRegisterd(exchange);
                    break;
                default:
                    httpUtils.enviarRespuesta(exchange,401,"Direccion no valida");
            }
        }catch (Exception e){
            httpUtils.enviarRespuesta(exchange, 500, e.getMessage());
        }
    }
    private void handlerPostLogin(HttpExchange exchange) throws IOException {
        String requestBody = httpUtils.leerRequestBody(exchange);
        JSONObject obj = new JSONObject(requestBody);

        users user = new users();
        user.setPassword(obj.getString("password"));
        if (obj.has("login")) {
            user.setLogin(obj.getString("login"));
        }else if (obj.has("email")) {
            user.setEmail(obj.getString("email"));
        }

        login(user, exchange);
    }

    private void login (users user, HttpExchange exchange) throws IOException{
        String encryptor = Utils.encrypt(user.getPassword());
        user.setPassword(encryptor);
        if (user.getLogin() != null) {
            if (!dao.validacion(user)){
                httpUtils.enviarRespuesta(exchange, 401 , "no se encontro la cuenta revise");
                return ;
            }
        }else if (user.getEmail() != null){
            if (!dao.validacion2(user)){
                httpUtils.enviarRespuesta(exchange, 401 , "no se encontro la cuenta revise");
                return ;
            }
        }

        String token = JwtUtils.generateToken(user);
        if (token==null){
            httpUtils.enviarRespuesta(exchange, 400 , "problemas con el token");
            return ;
        }

        JSONObject obj = new JSONObject("{\"token\":\""+token+"\"}");
        httpUtils.enviarRespuesta(exchange, 200 , obj.toString());
    }

    private void handlerPostRegisterd(HttpExchange exchange) throws IOException{
        String requestBody = httpUtils.leerRequestBody(exchange);
        JSONObject obj = new JSONObject(requestBody);

        if(obj.getString("login").isEmpty() || obj.getString("password").isEmpty()
                ||obj.getString("passwordConfirm").isEmpty() || obj.getString("email").isEmpty()
                || obj.getString("name").isEmpty() || obj.getString("tipo").isEmpty()){
            httpUtils.enviarRespuesta(exchange, 400 , "todos los campos tiene que estar llenos");
            return ;
        }
        if (!Utils.valCorreo(obj.getString("email"))){
            httpUtils.enviarRespuesta(exchange, 400 , "email incorrecto");
            return ;
        }
        if (!Utils.ValPassword(obj.getString("password"))){
            httpUtils.enviarRespuesta(exchange, 400 , "password incorrecto");
            return ;
        }

        users user = new users();
        user.setLogin(obj.getString("login"));
        if (!obj.getString("password").equals(obj.getString("passwordConfirm"))) {
            httpUtils.enviarRespuesta(exchange, 400 , "problemas con el password");
        }
        user.setPassword(Utils.encrypt(obj.getString("password")));
        user.setEmail(obj.getString("email"));
        user.setName(obj.getString("name"));
        user.setNumeroDocumento(obj.getString("documento"));
        user.getCuenta().setTipoCuenta(obj.getString("tipo"));
        crearCuenta(exchange, user);
    }

    private void crearCuenta(HttpExchange exchange, users user) throws IOException{
        user.getCuenta().setNumCuenta(numCuenta());
        user.setActivo("Y");
        if (!cuentaDao.insert(user)){
            httpUtils.enviarRespuesta(exchange, 400, "Usuario ya registrado");
            return;
        }
        user.getCuenta().setSaldo(new BigDecimal("1000000.00"));
        JSONObject obj = new JSONObject(user);
        httpUtils.enviarRespuesta(exchange, 200, obj.toString());
    }

    private static String  numCuenta(){
        Random random = new Random();
        int primerNumero = random.nextInt(9)+1;
        long restoNumeros = random.nextLong(1_000_000_000_00L);
        String numero = String.format("%1d%011d", primerNumero, restoNumeros);
        if (!cuentaDao.validacionNumCuneta(numero)){
            return numCuenta();
        }
        return numero;
    }

}
