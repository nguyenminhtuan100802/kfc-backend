package com.codegym.kfcbackend.configuration;

import com.codegym.kfcbackend.filter.JwtTokenFilter;
import com.codegym.kfcbackend.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenFilter jwtTokenFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;


    public WebSecurityConfig(CustomUserDetailsService userDetailsService,
                             JwtTokenFilter jwtTokenFilter,
                             CustomAccessDeniedHandler accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {
                })
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(POST, "/users/login").permitAll()
                            .requestMatchers(POST, "/users/change-default-password").permitAll()
                            .requestMatchers(POST, "/users/create-employee").permitAll()

                            .requestMatchers(GET, "/products/**").permitAll()
                            .requestMatchers(POST, "/products").hasAnyRole("ADMIN")
                            .requestMatchers(PUT, "/products/*").hasAnyRole("ADMIN")
                            .requestMatchers(DELETE, "/products/*").hasAnyRole("ADMIN")

                            .requestMatchers(GET, "/combos/**").permitAll()
                            .requestMatchers(POST, "/combos").hasAnyRole("ADMIN")
                            .requestMatchers(PUT, "/combos/*").hasAnyRole("ADMIN")
                            .requestMatchers(DELETE, "/combos/*").hasAnyRole("ADMIN")

                            .requestMatchers(POST, "/bills").permitAll()
                            .requestMatchers(GET, "/bills/**").hasAnyRole("ADMIN")
                            .requestMatchers(POST, "/bills/summary-report").hasAnyRole("ADMIN")

                            .requestMatchers(GET, "/ingredient-categories/**").hasAnyRole("ADMIN")
                            .requestMatchers(POST, "/ingredient-categories/**").hasAnyRole("ADMIN")
                            .requestMatchers(PUT, "/ingredient-categories/**").hasAnyRole("ADMIN")
                            .requestMatchers(DELETE, "/ingredient-categories/**").hasAnyRole("ADMIN")

                            .requestMatchers(GET, "/combo-categories/**").hasAnyRole("ADMIN")
                            .requestMatchers(POST, "/combo-categories/**").hasAnyRole("ADMIN")
                            .requestMatchers(PUT, "/combo-categories/**").hasAnyRole("ADMIN")
                            .requestMatchers(DELETE, "/combo-categories/**").hasAnyRole("ADMIN")

                            .requestMatchers(GET, "/product-categories/**").hasAnyRole("ADMIN")
                            .requestMatchers(POST, "/product-categories/**").hasAnyRole("ADMIN")
                            .requestMatchers(PUT, "/product-categories/**").hasAnyRole("ADMIN")
                            .requestMatchers(DELETE, "/product-categories/**").hasAnyRole("ADMIN")

                            .requestMatchers(GET, "/unit-of-measures/**").hasAnyRole("ADMIN")
                            .requestMatchers(POST, "/unit-of-measures/**").hasAnyRole("ADMIN")
                            .requestMatchers(PUT, "/unit-of-measures/**").hasAnyRole("ADMIN")
                            .requestMatchers(DELETE, "/unit-of-measures/**").hasAnyRole("ADMIN")

                            .requestMatchers(GET, "/ingredients/**").hasAnyRole("ADMIN")
                            .requestMatchers(POST, "/ingredients/**").hasAnyRole("ADMIN")
                            .requestMatchers(PUT, "/ingredients/**").hasAnyRole("ADMIN")
                            .requestMatchers(DELETE, "/ingredients/**").hasAnyRole("ADMIN")

                            .requestMatchers(GET, "/stock-entries/**").hasAnyRole("ADMIN")
                            .requestMatchers(POST, "/stock-entries/**").hasAnyRole("ADMIN")
                            .requestMatchers(PUT, "/stock-entries/**").hasAnyRole("ADMIN")
                            .requestMatchers(DELETE, "/stock-entries/**").hasAnyRole("ADMIN")

                            .requestMatchers(GET, "/roles").hasAnyRole("ADMIN")
                            .requestMatchers(POST, "/roles").hasAnyRole("ADMIN")

                            .anyRequest().authenticated();
                })
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler) // xử lý 403
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}
