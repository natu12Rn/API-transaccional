package org.example.dto;


import org.example.config.Database;
import org.example.models.transaccion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class transaccionDAO {

    public List<transaccion> buscarTodas() {
        List<transaccion> transaccions = new ArrayList<>();
        String sql = "select t.transaccion_id, c.numero_cuenta ,t.tipo_transaccion, t.monto ,t.cuenta_destino, t.descripcion, t.fecha_transaccion\n" +
                "from transaccion t\n" +
                "inner join public.cuenta c on c.cuenta_id = t.cuenta_id ORDER BY fecha_transaccion DESC";
        try(Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement()){

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                transaccion transaccion = new transaccion();
                transaccion.setTransaccionId(rs.getInt("transaccion_id"));
                transaccion.setNumcuenta(rs.getString("numero_cuenta"));
                transaccion.setTipoTransaccion(rs.getString("tipo_transaccion"));
                transaccion.setMonto(rs.getBigDecimal("monto"));
                transaccion.setCuentaDestino(rs.getString("cuenta_destino"));
                transaccion.setDescripcion(rs.getString("descripcion"));
                transaccion.setFechaTransaccion(rs.getTimestamp("fecha_transaccion"));
                transaccions.add(transaccion);
            }

            stmt.close();
            conn.close();
            return transaccions;

        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<transaccion>  buscarTransacciones(String numCuenta){
        List<transaccion> transaccions = new ArrayList<>();
        String sql = "SELECT t.transaccion_id, c.numero_cuenta, t.tipo_transaccion, t.monto, t.cuenta_destino, t.descripcion, t.fecha_transaccion FROM transaccion t\n" +
                "inner join public.cuenta c on c.cuenta_id = t.cuenta_id\n" +
                "where t.cuenta_id = (SELECT cuenta_id from  cuenta where numero_cuenta = ?)";
        try(    Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, numCuenta);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                transaccion transaccion = new transaccion();
                transaccion.setTransaccionId(rs.getInt("transaccion_id"));
                transaccion.setNumcuenta(rs.getString("numero_cuenta"));
                transaccion.setTipoTransaccion(rs.getString("tipo_transaccion"));
                transaccion.setMonto(rs.getBigDecimal("monto"));
                transaccion.setCuentaDestino(rs.getString("cuenta_destino"));
                transaccion.setDescripcion(rs.getString("descripcion"));
                transaccion.setFechaTransaccion(rs.getTimestamp("fecha_transaccion"));
                transaccions.add(transaccion);
            }

            return transaccions;

        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    public void getCuenta(transaccion transaccion,String numCuenta){
        String sql= "SELECT cuenta_id FROM cuenta WHERE numero_cuenta = ?";
        try(    Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, numCuenta);
            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                throw new RuntimeException("El cuenta no existe");
            }

            transaccion.setCuentaId(rs.getInt("cuenta_id"));

        }catch (SQLException e){
            e.printStackTrace();
            return;
        }
    }

    public boolean Deposito(transaccion transaccion ,String numCuenta){
        String sql= "CALL sp_deposito(?,?,?)";
        try(    Connection conn = Database.getConnection();
                CallableStatement cstmt = conn.prepareCall(sql)){

            cstmt.setBigDecimal(1, transaccion.getMonto());
            cstmt.setString(2, numCuenta );
            cstmt.registerOutParameter(3, java.sql.Types.BOOLEAN);

            cstmt.execute();

            return cstmt.getBoolean(3);


        }catch (SQLException e){

            e.printStackTrace();

            return false;
        }

    }

    public Boolean retirar(transaccion transaccion, String numCuenta){
        String sql= "CALL sp_retiro(?,?,?,?)";
        try(    Connection conn = Database.getConnection();
                CallableStatement cstmt = conn.prepareCall(sql)){

            cstmt.setBigDecimal(1, transaccion.getMonto());
            cstmt.setString(2, numCuenta);
            cstmt.setString(3, transaccion.getDescripcion());
            cstmt.registerOutParameter(4, Types.BOOLEAN);

            cstmt.execute();

            return cstmt.getBoolean(4);

        }catch(SQLException e){
            e.printStackTrace();

            return false;
        }
    }

    public  boolean transferencia(transaccion transaccion,String numCuenta) {
        String sql= "CALL sp_transaccion(?,?,?,?,?)";
        try(    Connection conn = Database.getConnection();
                CallableStatement cstmt = conn.prepareCall(sql)){

            cstmt.setBigDecimal(1, transaccion.getMonto());
            cstmt.setString(2, numCuenta);
            cstmt.setString(3, transaccion.getCuentaDestino());
            cstmt.setString(4, transaccion.getDescripcion());
            cstmt.registerOutParameter(5, Types.BOOLEAN);

            cstmt.execute();

            return  cstmt.getBoolean(5);

        }catch(SQLException e){
            e.printStackTrace();

            return false;
        }
    }

}
