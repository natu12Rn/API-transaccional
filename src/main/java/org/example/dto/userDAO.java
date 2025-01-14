package org.example.dto;

import org.example.config.Database;
import org.example.models.users;
import org.example.utils.LogsUtils;

import java.sql.*;

public class userDAO {

    public boolean validacion (users user){
        String sql = "SELECT s.name, s.login, s.priv_admin, c.numero_cuenta\n" +
                "FROM sec_users s\n" +
                "         LEFT JOIN cuenta c ON s.login = c.login_usuario\n" +
                "where s.pswd = ? and s.login=? and s.active = 'Y'";
        try(Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            LogsUtils.logInfo("Ejecutando query -> \n " + sql);

            ps.setString(1, user.getPassword());
            ps.setString(2, user.getLogin());

            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                LogsUtils.logWarn("No se encontro el usuario");
                return false;
            }

            user.setName(rs.getString("name"));
            user.getCuenta().setNumCuenta(rs.getString("numero_cuenta"));
            user.setPrivAdmin(rs.getString("priv_admin"));

            return true;
        }catch (SQLException e){
            LogsUtils.logError(e.getMessage());
            return false;
        }

    }

    public boolean validacion2 (users user){
        String sql = "SELECT s.name, s.login, s.priv_admin, c.numero_cuenta\n" +
                "FROM sec_users s\n" +
                "         LEFT JOIN cuenta c ON s.login = c.login_usuario\n" +
                "where s.pswd = ? and s.email=? and s.active = 'Y'";
        try(Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){

            LogsUtils.logInfo("Ejecutando query -> \n " + sql);

            ps.setString(1, user.getPassword());
            ps.setString(2, user.getEmail());

            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                LogsUtils.logWarn("No se encontro el usuario");
                return false;
            }

            user.setName(rs.getString("name"));
            user.getCuenta().setNumCuenta(rs.getString("numero_cuenta"));
            user.setPrivAdmin(rs.getString("priv_admin"));

            return true;
        }catch (SQLException e){
            LogsUtils.logError(e.getMessage());
            return false;
        }
    }
}
