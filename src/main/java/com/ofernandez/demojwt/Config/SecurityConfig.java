package com.ofernandez.demojwt.Config;

import com.ofernandez.demojwt.Jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Deshabilitamos la seguridad por defecto de Spring Security
        return http
            .csrf(csrf ->
                csrf
                .disable())
            //Agregamos las rutas de /Auth que tendrán todos los permisos, y cualquier otra ruta diferente debe autenticarse para acceder a ellas.
            .authorizeHttpRequests(authRequest ->
                authRequest
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
            )
                //Agregamos las políticas para el manejo de las sesiones
                .sessionManagement(sessionManager ->
                    sessionManager
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
