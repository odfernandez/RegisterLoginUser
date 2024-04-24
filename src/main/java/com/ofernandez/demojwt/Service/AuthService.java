package com.ofernandez.demojwt.Service;

import com.ofernandez.demojwt.Auth.AuthResponse;
import com.ofernandez.demojwt.Auth.LoginRequest;
import com.ofernandez.demojwt.Auth.RegisterRequest;
import com.ofernandez.demojwt.Repository.UserRepository;
import com.ofernandez.demojwt.Service.JwtService;
import com.ofernandez.demojwt.User.Role;
import com.ofernandez.demojwt.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service //Le indicamos a spring boot que esta clase se manejara como capa de servicio.
@RequiredArgsConstructor
public class AuthService {

    //Realizamos las declaraciones para las inyecciones de dependencia.
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    //Con este método realiza el login un usuario
    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow(()->
                new UsernameNotFoundException("Verify your username or password"));
        String token = jwtService.getToken(user);

        //Nos retorna un Objeto de tipo AuthResponse que nos da un token y con el mismo lo pasamos como parámetro
        //en la autorización que nos permitirá ingresar a las páginas que necesiten autenticación.
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    //Con este método registramos un usuario
    public AuthResponse register(RegisterRequest request) {
        //Usamos el patron Builder para crear un usuario y capturamos los datos del request
        User user = User.builder()
                .username(request.getUsername())
                //Al capturar el password se le pasa de parámetro al método passwordEncoder
                //para encriptar el password.
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .country(request.getCountry())
                .role(Role.USER)
                .build();
        userRepository.save(user);

        //Retornamos un Objeto de tipo AuthResponse que nos devuelve un token
        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }
}
