package com.codigo.security.controller;

import com.codigo.security.request.SignInRequest;
import com.codigo.security.request.SignUpRequest;
import com.codigo.security.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final UsuarioService usuarioService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request){
        usuarioService.signup(request);
        return ResponseEntity.ok("Usuario creado exitosamente");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest request){
        return ResponseEntity.ok(usuarioService.signin(request));
    }

    @GetMapping("/valid")
    public ResponseEntity<Boolean> valid() {
        return ResponseEntity.ok(true);
    }
}
