package com.poc.parkapi.config;

import com.poc.parkapi.jwt.JwtAuthenticationEntryPoint;
import com.poc.parkapi.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import java.util.Arrays;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

@EnableMethodSecurity
@EnableWebMvc
@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                antMatcher(HttpMethod.POST, "/api/v1/users"),
                                antMatcher(HttpMethod.POST, "/api/v1/auth/login")
                        ).permitAll()
                        .requestMatchers(
                                antMatcher("/docs/index.html"),
                                antMatcher("/docs-park.html"),
                                antMatcher("/docs-park/**"),
                                antMatcher("/v3/api-docs/**"),
                                antMatcher("/swagger-ui-custom.html"),
                                antMatcher("/swagger-ui.html"),
                                antMatcher("/swagger-ui/index.html"),
                                antMatcher("/swagger-ui/**"),
                                antMatcher("/**.html"),
                                antMatcher("/webjars/**"),
                                antMatcher("/configuration/**"),
                                antMatcher("/swagger-resources/**")
                        ).permitAll()
                        .anyRequest().authenticated()
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .build();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
