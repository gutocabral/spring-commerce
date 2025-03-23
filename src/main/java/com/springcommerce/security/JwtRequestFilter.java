package com.springcommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtRequestFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Obtém o cabeçalho Authorization
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Verifica se o cabeçalho existe e começa com "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extrai o token (remove "Bearer ")
            jwt = authorizationHeader.substring(7);
            try {
                // Extrai o username do token
                username = jwtTokenUtil.getUsernameFromToken(jwt);
            } catch (Exception e) {
                logger.error("Erro ao extrair username do token", e);
            }
        }

        // Se o username foi extraído e não há autenticação no contexto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carrega os detalhes do usuário
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Valida o token
            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                // Cria um token de autenticação
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Define detalhes da requisição no token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Define a autenticação no contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continua a cadeia de filtros
        chain.doFilter(request, response);
    }
}
