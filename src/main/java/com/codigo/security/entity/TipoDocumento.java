package com.codigo.security.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class TipoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipoDocumento;

    private String codigo;
    private String descripcion;
    private Boolean estado;
}
