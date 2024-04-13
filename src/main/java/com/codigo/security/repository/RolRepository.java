package com.codigo.security.repository;

import com.codigo.security.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    Optional<Rol> findByCodigo(String codigo);
}
