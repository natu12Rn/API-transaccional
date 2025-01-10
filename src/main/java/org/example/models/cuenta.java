package org.example.models;

import java.math.BigDecimal;

public class cuenta {
    private String numCuenta;
    private BigDecimal saldo;
    private String tipoCuenta;


    public cuenta() {
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    @Override
    public String toString() {
        return "cuenta{" +
                "numCuenta='" + numCuenta + '\'' +
                ", saldo=" + saldo +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                '}';
    }
}
