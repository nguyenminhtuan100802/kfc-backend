package com.codegym.kfcbackend.configuration;

import com.codegym.kfcbackend.constant.PermissionConstant;
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
                            .requestMatchers(POST, "/users/create-employee").hasAuthority(PermissionConstant.EMPLOYEE_CREATE)

                            // Product
                            .requestMatchers(GET, "/products/**").hasAuthority(PermissionConstant.PRODUCT_VIEW)
                            .requestMatchers(POST, "/products").hasAuthority(PermissionConstant.PRODUCT_CREATE)
                            .requestMatchers(PUT, "/products/*").hasAuthority(PermissionConstant.PRODUCT_UPDATE)
                            .requestMatchers(DELETE, "/products/*").hasAuthority(PermissionConstant.PRODUCT_DELETE)

                            // Combo
                            .requestMatchers(GET, "/combos/**").hasAuthority(PermissionConstant.COMBO_VIEW)
                            .requestMatchers(POST, "/combos").hasAuthority(PermissionConstant.COMBO_CREATE)
                            .requestMatchers(PUT, "/combos/*").hasAuthority(PermissionConstant.COMBO_UPDATE)
                            .requestMatchers(DELETE, "/combos/*").hasAuthority(PermissionConstant.COMBO_DELETE)

                            // Bill
                            .requestMatchers(POST, "/bills").hasAuthority(PermissionConstant.BILL_CREATE)
                            .requestMatchers(GET, "/bills/**").hasAuthority(PermissionConstant.BILL_VIEW)
                            .requestMatchers(POST, "/bills/summary-report").hasAuthority(PermissionConstant.BILL_VIEW)

                            // Ingredient Category
                            .requestMatchers(GET, "/ingredient-categories/**").hasAuthority(PermissionConstant.INGREDIENT_CATEGORY_VIEW)
                            .requestMatchers(POST, "/ingredient-categories/**").hasAuthority(PermissionConstant.INGREDIENT_CATEGORY_CREATE)
                            .requestMatchers(PUT, "/ingredient-categories/**").hasAuthority(PermissionConstant.INGREDIENT_CATEGORY_UPDATE)
                            .requestMatchers(DELETE, "/ingredient-categories/**").hasAuthority(PermissionConstant.INGREDIENT_CATEGORY_DELETE)

                            // Combo Category
                            .requestMatchers(GET, "/combo-categories/**").hasAuthority(PermissionConstant.COMBO_CATEGORY_VIEW)
                            .requestMatchers(POST, "/combo-categories/**").hasAuthority(PermissionConstant.COMBO_CATEGORY_CREATE)
                            .requestMatchers(PUT, "/combo-categories/**").hasAuthority(PermissionConstant.COMBO_CATEGORY_UPDATE)
                            .requestMatchers(DELETE, "/combo-categories/**").hasAuthority(PermissionConstant.COMBO_CATEGORY_DELETE)

                            // Product Category
                            .requestMatchers(GET, "/product-categories/**").hasAuthority(PermissionConstant.PRODUCT_CATEGORY_VIEW)
                            .requestMatchers(POST, "/product-categories/**").hasAuthority(PermissionConstant.PRODUCT_CATEGORY_CREATE)
                            .requestMatchers(PUT, "/product-categories/**").hasAuthority(PermissionConstant.PRODUCT_CATEGORY_UPDATE)
                            .requestMatchers(DELETE, "/product-categories/**").hasAuthority(PermissionConstant.PRODUCT_CATEGORY_DELETE)

                            // Unit of Measure
                            .requestMatchers(GET, "/unit-of-measures/**").hasAuthority(PermissionConstant.UNIT_OF_MEASURE_VIEW)
                            .requestMatchers(POST, "/unit-of-measures/**").hasAuthority(PermissionConstant.UNIT_OF_MEASURE_CREATE)
                            .requestMatchers(PUT, "/unit-of-measures/**").hasAuthority(PermissionConstant.UNIT_OF_MEASURE_UPDATE)
                            .requestMatchers(DELETE, "/unit-of-measures/**").hasAuthority(PermissionConstant.UNIT_OF_MEASURE_DELETE)

                            // Ingredient
                            .requestMatchers(GET, "/ingredients/**").hasAuthority(PermissionConstant.INGREDIENT_VIEW)
                            .requestMatchers(POST, "/ingredients/**").hasAuthority(PermissionConstant.INGREDIENT_CREATE)
                            .requestMatchers(PUT, "/ingredients/**").hasAuthority(PermissionConstant.INGREDIENT_UPDATE)
                            .requestMatchers(DELETE, "/ingredients/**").hasAuthority(PermissionConstant.INGREDIENT_DELETE)

                            // Stock Entry
                            .requestMatchers(GET, "/stock-entries/**").hasAuthority(PermissionConstant.STOCK_ENTRY_VIEW)
                            .requestMatchers(POST, "/stock-entries/**").hasAuthority(PermissionConstant.STOCK_ENTRY_CREATE)
                            .requestMatchers(PUT, "/stock-entries/**").hasAuthority(PermissionConstant.STOCK_ENTRY_UPDATE)
                            .requestMatchers(DELETE, "/stock-entries/**").hasAuthority(PermissionConstant.STOCK_ENTRY_DELETE)

                            // Role
                            .requestMatchers(GET, "/roles").hasAuthority(PermissionConstant.ROLE_VIEW)
                            .requestMatchers(POST, "/roles").hasAuthority(PermissionConstant.ROLE_CREATE)
                            .requestMatchers(PUT, "/roles/**").hasAuthority(PermissionConstant.ROLE_UPDATE)
                            .requestMatchers(DELETE, "/roles/**").hasAuthority(PermissionConstant.ROLE_DELETE)

//                            Role permission
                            .requestMatchers(GET, "/role-permissions").hasAuthority(PermissionConstant.ROLE_PERMISSION_VIEW)
                            .requestMatchers(PUT, "/role-permissions").hasAuthority(PermissionConstant.ROLE_PERMISSION_UPDATE)

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
