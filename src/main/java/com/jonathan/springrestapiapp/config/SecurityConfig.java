package com.jonathan.springrestapiapp.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jonathan.springrestapiapp.security.JwtAuthFilter;
import com.jonathan.springrestapiapp.security.JwtService;
import com.jonathan.springrestapiapp.service.impl.UsuarioServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioServiceImpl usuarioService;

/*     @Value("${spring.h2.console.path}")
    private String h2ConsolePath; */

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http // Adiciona a configuração de CORS aqui
            .authorizeHttpRequests((authz) ->                                     
                authz
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers(HttpMethod.PATCH, "/api/usuarios/**").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/usuarios/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/admin/**").hasAnyRole("ADMIN", "MODERADOR")
                    .requestMatchers(HttpMethod.PATCH, "/api/admin/**").hasAnyRole("ADMIN", "MODERADOR")
                    .requestMatchers(HttpMethod.POST, "/api/admin/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST,"/api/post/**").authenticated()
                    .requestMatchers(HttpMethod.PATCH,"/api/post/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE,"/api/post/**").authenticated()
                    .requestMatchers(HttpMethod.GET,"/api/post/**").permitAll()
                    .requestMatchers("/api/profile/**").authenticated()
                    .requestMatchers("/api/person/**").authenticated()
                    .requestMatchers("/image/**").permitAll()
                    .requestMatchers(AUTH_STRINGS).permitAll()
                    .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(
                jwtFilter(), 
                UsernamePasswordAuthenticationFilter.class)
           
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }


    private static final String[] AUTH_STRINGS = {
        "/api/v1/auth/**",
        "/v3/api-docs/**",
        "/v3/api-docs.yaml",
        "/swagger-ui/**",
        "/**",
        "/h2/**",
        "/swagger-ui.html"
    };

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:80"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
