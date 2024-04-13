package com.codigo.security.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReniecResponse {
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tipoDocumento;
    private String numeroDocumento;
    private String digitoVerificador;
}