package com.empresa.clientes_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // se for uma API stateless, pode desabilitar CSRF
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/clientes/**").permitAll()  // libera /clientes para todos
                        .anyRequest().authenticated()              // outras rotas continuam protegidas
                )
                .httpBasic();  // mantém HTTP Basic para demais endpoints, se quiser

        return http.build();
    }
}
