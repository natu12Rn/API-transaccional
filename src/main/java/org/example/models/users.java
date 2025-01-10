package org.example.models;

public class users {
    private String login;
    private String name;
    private String email;
    private String numeroDocumento;
    private String password;
    private String privAdmin;
    private String activo;
    private cuenta cuenta = new cuenta();

    public users() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivAdmin() {
        return privAdmin;
    }

    public void setPrivAdmin(String privAdmin) {
        this.privAdmin = privAdmin;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(cuenta cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public String toString() {
        return "users{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", password='" + password + '\'' +
                ", privAdmin='" + privAdmin + '\'' +
                ", activo='" + activo + '\'' +
                ", cuenta=" + cuenta +
                '}';
    }
}
