package com.codigo.security.service;


import com.codigo.security.client.ReniecClient;
import com.codigo.security.entity.Rol;
import com.codigo.security.entity.TipoDocumento;
import com.codigo.security.entity.Usuario;
import com.codigo.security.repository.RolRepository;
import com.codigo.security.repository.TipoDocumentoRepository;
import com.codigo.security.repository.UsuarioRepository;
import com.codigo.security.request.SignInRequest;
import com.codigo.security.request.SignUpRequest;
import com.codigo.security.response.AuthenticationResponse;
import com.codigo.security.response.ReniecResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final RolRepository rolRepository;
    private final JWTService jwtService;
    private final ReniecClient reniecClient;
    private final String tokenApi;

    public UsuarioService(@Lazy AuthenticationManager authenticationManager,
                          UsuarioRepository usuarioRepository, JWTService jwtService,
                          TipoDocumentoRepository tipoDocumentoRepository,
                          RolRepository rolRepository, ReniecClient reniecClient,
                          @Value("${token.api}") String tokenApi) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.rolRepository = rolRepository;
        this.reniecClient = reniecClient;
        this.tokenApi = tokenApi;
    }

    public AuthenticationResponse signin(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.usuario(), signInRequest.password()));
        var user = usuarioRepository.findByUsuario(signInRequest.usuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no valido"));

        var jwt = jwtService.generateToken(user);
        return new AuthenticationResponse(jwt);
    }

    public void signup(SignUpRequest request) {
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(request.idTipoDocumento())
                .orElseThrow(() -> new EntityNotFoundException("Tipo documento no existe"));
        ReniecResponse reniecResponse = getExecution(request.nroDocumento());
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(request, usuario);
        usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        usuario.setEstado(Boolean.TRUE);
        usuario.setTipoDocumento(tipoDocumento);
        usuario.setNombres(reniecResponse.getNombres());
        usuario.setApellidos(reniecResponse.getApellidoPaterno() + " " + reniecResponse.getApellidoMaterno());
        Rol rol = rolRepository.findById(request.idRol())
                .orElseThrow(() -> new EntityNotFoundException("Rol no existe"));
        usuario.setRol(rol);
        usuarioRepository.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    private ReniecResponse getExecution(String numero){
        String authorization = "Bearer "+tokenApi;
        return reniecClient.getInfo(numero,authorization);
    }

}
