package com.codigo.security.entity;

public enum Roles {
    ADMINISTRADOR("001"), PROPIETARIO("002"), CLIENTE("003");
    private final String codigo;
    Roles(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return this.codigo;
    }
}
