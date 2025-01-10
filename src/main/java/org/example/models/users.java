package org.example.models;

public class users {
    private String login;
    private String name;
    private String email;
    private String password;
    private String privAdmin;
    private cuenta cuenta = new cuenta();

    public cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(cuenta cuenta) {
        this.cuenta = cuenta;
    }

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

    @Override
    public String toString() {
        return "users{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", privAdmin='" + privAdmin + '\'' +
                ", cuenta=" + cuenta +
                '}';
    }
}
