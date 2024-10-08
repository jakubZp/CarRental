package com.example.carrental.config;

import com.example.carrental.config.filters.ActiveStatusFilter;
import com.example.carrental.config.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final ActiveStatusFilter activeStatusFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final CorsConfigurationSource corsConfigurationSource;
    public static final String[] PUBLIC_PATHS = {"/api/v1/auth/**",
            "/api/v1/cars/**",
            "/swagger-ui/**",
            "v3/api-docs/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http//TODO
                .cors(c -> c.configurationSource(corsConfigurationSource))
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(new Http403ForbiddenEntryPoint()))
                .csrf()
                .disable()
                .authorizeHttpRequests()
                    .requestMatchers(PUBLIC_PATHS)
                    .permitAll()
                    .requestMatchers("api/v1/priceupdates")
                    .hasAnyAuthority("EMPLOYEE", "ADMIN")
                    .anyRequest()
                    .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(activeStatusFilter, JwtAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        return http.build();
    }
}
