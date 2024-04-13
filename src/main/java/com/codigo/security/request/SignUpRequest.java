package com.codigo.security.request;


public record SignUpRequest (
        String usuario,
        String password,
//        String nombres,
//        String apellidos,
        Integer idTipoDocumento,
        Integer idRol,
        String nroDocumento,
        String direccion,
        String telefono,
        String correo
){
}
