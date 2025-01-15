package org.example.dto;


import org.example.config.database.ConnectionManager;
import org.example.models.transaccion;
import org.example.utils.LogsUtils;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class transaccionDAO {
    private static final ConnectionManager connectionManager = ConnectionManager.getInstance();

    public List<transaccion> buscarTodas() {
        List<transaccion> transaccions = new ArrayList<>();
        String sql = "select t.transaccion_id, c.numero_cuenta ,t.tipo_transaccion, t.monto ,t.cuenta_destino, t.descripcion, t.fecha_transaccion from transaccion t inner join public.cuenta c on c.cuenta_id = t.cuenta_id ORDER BY fecha_transaccion DESC";
        try(Connection conn = connectionManager.getConnection();
            Statement stmt = conn.createStatement()){

            LogsUtils.logProcesInfo("Ejecutando query --> " + sql);

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

        }catch (Exception e) {
            LogsUtils.logProcesError(e.getMessage());
            return null;
        }
    }

    public List<transaccion>  buscarTransacciones(String numCuenta){
        List<transaccion> transaccions = new ArrayList<>();
        String sql = "SELECT t.transaccion_id, c.numero_cuenta, t.tipo_transaccion, t.monto, t.cuenta_destino, t.descripcion, t.fecha_transaccion FROM transaccion t inner join public.cuenta c on c.cuenta_id = t.cuenta_id where t.cuenta_id = (SELECT cuenta_id from  cuenta where numero_cuenta = ?)";

        try(Connection conn = connectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            LogsUtils.logProcesInfo("Ejecutando query --> " + sql);

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

        }catch (Exception e){
            LogsUtils.logProcesError(e.getMessage());
            return null;
        }

    }

    public void getCuenta(transaccion transaccion,String numCuenta){
        String sql= "SELECT cuenta_id FROM cuenta WHERE numero_cuenta = ?";
        try(Connection conn = connectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            LogsUtils.logProcesInfo("Ejecutando query --> " + sql);

            ps.setString(1, numCuenta);
            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                LogsUtils.logError("El cuenta no existe");
                throw new RuntimeException("El cuenta no existe");
            }

            transaccion.setCuentaId(rs.getInt("cuenta_id"));

        }catch (Exception e){
            LogsUtils.logError(e.getMessage());
        }
    }

    public boolean Deposito(transaccion transaccion ,String numCuenta){
        String sql= "CALL sp_deposito(?,?,?)";
        try(Connection conn = connectionManager.getConnection();
            CallableStatement cstmt = conn.prepareCall(sql)){

            LogsUtils.logProcesInfo("Ejecutando SP --> " + sql);

            cstmt.setBigDecimal(1, transaccion.getMonto());
            cstmt.setString(2, numCuenta );
            cstmt.registerOutParameter(3, java.sql.Types.BOOLEAN);

            cstmt.execute();
            LogsUtils.logProcesInfo("Respuesta del SP --> " + cstmt.getBoolean(3));
            return cstmt.getBoolean(3);

        }catch (Exception e){
            LogsUtils.logProcesError(e.getMessage());
            return false;
        }

    }

    public Boolean retirar(transaccion transaccion, String numCuenta){
        String sql= "CALL sp_retiro(?,?,?,?)";
        try(Connection conn = connectionManager.getConnection();
            CallableStatement cstmt = conn.prepareCall(sql)){

            LogsUtils.logProcesInfo("Ejecutando SP --> " + sql);

            cstmt.setBigDecimal(1, transaccion.getMonto());
            cstmt.setString(2, numCuenta);
            cstmt.setString(3, transaccion.getDescripcion());
            cstmt.registerOutParameter(4, Types.BOOLEAN);

            cstmt.execute();
            LogsUtils.logProcesInfo("Respuesta del SP --> " + cstmt.getBoolean(4));
            return cstmt.getBoolean(4);

        }catch(Exception e){
            LogsUtils.logProcesError(e.getMessage());
            return false;
        }
    }

    public  boolean transferencia(transaccion transaccion,String numCuenta) {
        String sql= "CALL sp_transaccion(?,?,?,?,?)";
        try(Connection conn = connectionManager.getConnection();
            CallableStatement cstmt = conn.prepareCall(sql)){

            LogsUtils.logProcesInfo("Ejecutando SP --> " + sql);

            cstmt.setBigDecimal(1, transaccion.getMonto());
            cstmt.setString(2, numCuenta);
            cstmt.setString(3, transaccion.getCuentaDestino());
            cstmt.setString(4, transaccion.getDescripcion());
            cstmt.registerOutParameter(5, Types.BOOLEAN);

            cstmt.execute();
            LogsUtils.logProcesInfo("Respuesta del SP --> " + cstmt.getBoolean(5));
            return  cstmt.getBoolean(5);

        }catch(Exception e){
            LogsUtils.logProcesError(e.getMessage());
            return false;
        }
    }

}
