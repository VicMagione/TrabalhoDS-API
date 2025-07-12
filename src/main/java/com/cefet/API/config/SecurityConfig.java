package com.cefet.API.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.cefet.API.security.JwtAuthenticationFilter;
import com.cefet.API.services.ClienteDetailsService;

@Configuration
public class SecurityConfig {
    @Autowired
    private ClienteDetailsService clienteDetailsService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/clientes/**", "/contas/**",
                        "/lancamentos/**", "/auth/login"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // Acesso ao H2 Console
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        .requestMatchers(HttpMethod.POST, "/clientes").permitAll() // Permitir criação de usuário
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // Permitir endpoint de login
                        .requestMatchers(HttpMethod.GET, "/clientes").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/clientes/{id}").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/clientes/{id}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/clientes/cpf/{cpf}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/clientes/{id}").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/clientes/cpf/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/clientes/cpf/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/contas/verificar-chave-pix/{chave}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/contas/{id}").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/contas/{id}/saque").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/contas/{id}/deposito").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/contas/{id}/{id2}/transferencia")
                        .hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/contas/{pix1}/{pix2}/pix")
                        .hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/contas").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/contas").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/contas/cliente/cpf/{cpf}").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/contas/cliente/{id}").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/contas/cliente/cpf/{cpf}/saldo-total")
                        .hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/contas/cliente/{id}/saldo-total")
                        .hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/contas/**").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/contas/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/contas/cpf/{cpf}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/contas/**").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/contas/{id}/chave-limite").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/contas/cpf/{cpf}/chave-limite")
                        .hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/contas/{id}/limite").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/contas/cpf/{cpf}/chave-pix").hasAnyRole("GESTOR", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/lancamentos").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/lancamentos/{id}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/lancamentos").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/lancamentos/conta/{contaId}").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/lancamentos/transferir").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/lancamentos/pix").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/lancamentos/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/lancamentos/**").hasRole("ADMIN"))
                .headers(headers -> headers.frameOptions().disable()) // Para H2 Console
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // @Bean
    // SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // http.csrf(csrf -> csrf.disable())
    // .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // Permite
    // return http.build();
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("deprecation")
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(clienteDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}