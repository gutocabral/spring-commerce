package com.springcommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Desabilita CSRF para simplificar o desenvolvimento da API
                .authorizeHttpRequests(authorize -> authorize
                        // Permitir acesso ao console H2
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        // Permitir acesso a todos os endpoints da API
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/**")).permitAll()
                        // Qualquer outra requisição requer autenticação
                        .anyRequest().authenticated()
                )
                // Configuração para permitir que o console H2 funcione corretamente
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                );

        return http.build();
    }
}