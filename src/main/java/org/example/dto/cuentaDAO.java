package org.example.dto;

import io.fusionauth.jwt.domain.JWT;

import org.example.config.database.ConnectionManager;
import org.example.models.cuenta;
import org.example.models.users;
import org.example.utils.LogsUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class cuentaDAO {
    private static final ConnectionManager connectionManager = ConnectionManager.getInstance();

    public static cuenta CuentaPersonal(JWT jwt) {
        cuenta cuenta = new cuenta();
        String sql = "SELECT C.saldo, tc.nombre AS tipo_cuenta FROM cuenta c INNER JOIN public.tipo_cuenta tc on tc.tipo_cuenta_id = c.tipo_cuenta_id WHERE numero_cuenta= ?";

        try(Connection conn = connectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            LogsUtils.logProcesInfo("Ejecutando query --> " + sql);

            ps.setString(1, jwt.getString("numCuenta"));
            ResultSet rs = ps.executeQuery();

            if(!rs.next()) {
                LogsUtils.logProcesWarn("No se encontro la cuenta: "+ jwt.getString("numCuenta"));
                return null;

            }

            cuenta.setNumCuenta(jwt.getString("numCuenta"));
            cuenta.setSaldo(rs.getBigDecimal("saldo"));
            cuenta.setTipoCuenta(rs.getString("tipo_cuenta"));

            return cuenta;

        }catch (Exception e){
            LogsUtils.logProcesError(e.getMessage());
            return null;
        }
    }

    public static List<users> listCuentas() {
        List<users> cuentas = new ArrayList<>();
        String sql = "SELECT u.login, u.name, u.email, c.numero_cuenta, u.active ,tc.nombre AS tipo_cuenta FROM sec_users u LEFT JOIN public.cuenta c on u.login = c.login_usuario inner join public.tipo_cuenta tc on c.tipo_cuenta_id = tc.tipo_cuenta_id";
        try(Connection conn = connectionManager.getConnection();
            Statement stmt = conn.createStatement();){

            LogsUtils.logProcesInfo("Ejecutando query --> " + sql);

            stmt.execute(sql);
            ResultSet rs = stmt.executeQuery(sql);

            if(!rs.next()) {
                LogsUtils.logProcesWarn("No se encontraron cuentas");
                return null;
            }

            while(rs.next()) {
                users user = new users();
                user.setLogin(rs.getString("login"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setActivo(rs.getString("active"));
                user.getCuenta().setNumCuenta(rs.getString("numero_cuenta"));
                user.getCuenta().setTipoCuenta(rs.getString("tipo_cuenta"));
                cuentas.add(user);
            }

            return cuentas;

        }catch (Exception e){
            LogsUtils.logProcesError(e.getMessage());
            return null;
        }
    }

    public boolean insert(users user) {
        String sql = "CALL sp_crearcuenta(?,?,?,?,?,?,?,?,?)";
        try(Connection conn = connectionManager.getConnection();
            CallableStatement cstmt = conn.prepareCall(sql) ) {

            LogsUtils.logProcesInfo("Ejecutando SP -->" + sql);

            cstmt.setString(1,user.getLogin());
            cstmt.setString(2,user.getPassword());
            cstmt.setString(3, user.getName());
            cstmt.setString(4, user.getEmail());
            cstmt.setString(5, user.getCuenta().getNumCuenta());
            cstmt.setString(6,user.getCuenta().getTipoCuenta());
            cstmt.setString(7,user.getNumeroDocumento());
            cstmt.setString(8,user.getActivo());
            cstmt.registerOutParameter(9, java.sql.Types.BOOLEAN);

            cstmt.execute();

            boolean result = cstmt.getBoolean(9);
            LogsUtils.logProcesInfo("Cuenta creada -> " + result);
            return result;
        }catch (Exception e){
            LogsUtils.logProcesError(e.getMessage());
            return false;
        }
    }

    public boolean validacionNumCuneta(String numCuneta) {
        String sql = "SELECT valnumcuenta(?)";
        try(Connection conn = connectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            LogsUtils.logProcesInfo("Ejecutando query --> " + sql);

            pstmt.setString(1, numCuneta);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                LogsUtils.logProcesWarn("existencia de numero de cuenta -> "+ numCuneta);
                return false;
            }

            LogsUtils.logProcesInfo("Numero de cuenta valido -> "+ numCuneta);
            return true;
        }catch (Exception e){
            LogsUtils.logProcesError(e.getMessage());
            return false;
        }
    }
}
