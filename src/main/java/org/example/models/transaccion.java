package org.example.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class transaccion {
    private int transaccionId;
    private int cuentaId;
    private String numcuenta;
    private String tipoTransaccion;
    private BigDecimal monto;
    private Timestamp fechaTransaccion;
    private String descripcion;
    private String cuentaDestino;

    public transaccion() {
        this.transaccionId = transaccionId;
        this.cuentaId = cuentaId;
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
        this.fechaTransaccion = fechaTransaccion;
        this.descripcion = descripcion;
        this.cuentaDestino = cuentaDestino;
    }

    public int getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(int transaccionId) {
        this.transaccionId = transaccionId;
    }

    public int getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(int cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getNumcuenta() {

        return numcuenta;
    }

    public void setNumcuenta(String numcuenta) {
        this.numcuenta = numcuenta;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Timestamp getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(Timestamp fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(String cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    @Override
    public String toString() {
        return "transaccion{" +
                "transaccionId=" + transaccionId +
                ", cuentaId=" + cuentaId +
                ", tipoTransaccion='" + tipoTransaccion + '\'' +
                ", monto=" + monto +
                ", fechaTransaccion=" + fechaTransaccion +
                ", descripcion='" + descripcion + '\'' +
                ", cuentaDestino='" + cuentaDestino + '\'' +
                '}';
    }
}