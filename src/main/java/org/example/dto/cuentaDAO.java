package org.example.dto;

import io.fusionauth.jwt.domain.JWT;
import org.example.config.Database;
import org.example.models.cuenta;
import org.example.models.users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class cuentaDAO {
    public static cuenta CuentaPersonal(JWT jwt) {
        cuenta cuenta = new cuenta();
        String sql = "SELECT C.saldo, tc.nombre AS tipo_cuenta FROM cuenta c\n" +
                "INNER JOIN public.tipo_cuenta tc on tc.tipo_cuenta_id = c.tipo_cuenta_id\n" +
                "WHERE numero_cuenta= ?";

        try(Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, jwt.getString("numCuenta"));
            ResultSet rs = ps.executeQuery();

            if(!rs.next()) {
                return null;
            }

            cuenta.setNumCuenta(jwt.getString("numCuenta"));
            cuenta.setSaldo(rs.getBigDecimal("saldo"));
            cuenta.setTipoCuenta(rs.getString("tipo_cuenta"));

            return cuenta;

        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<users> listCuentas() {
        List<users> cuentas = new ArrayList<>();
        String sql = "SELECT u.login, u.name, u.email, c.numero_cuenta, u.active ,tc.nombre AS tipo_cuenta FROM sec_users u\n" +
                "LEFT JOIN public.cuenta c on u.login = c.login_usuario\n" +
                "inner join public.tipo_cuenta tc on c.tipo_cuenta_id = tc.tipo_cuenta_id";
        try(Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();){

            stmt.execute(sql);
            ResultSet rs = stmt.executeQuery(sql);

            if(!rs.next()) {
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

        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean insert(users user) {
        String sql = "CALL sp_crearcuenta(?,?,?,?,?,?,?,?,?)";
        try(Connection conn = Database.getConnection();
            CallableStatement cstmt = conn.prepareCall(sql) ) {

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

            return result;
        }catch (SQLException e){
            return false;
        }
    }

    public boolean validacionNumCuneta(String numCuneta) {
        String sql = "SELECT valnumcuenta(?)";
        try(Connection conn = Database.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, numCuneta);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return false;
            }

            return true;
        }catch (SQLException e){
            return false;
        }
    }
}
