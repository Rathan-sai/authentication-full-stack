package com.tech.programming.Authentication_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(customizer->customizer.disable())
                .authorizeHttpRequests(authorize-> authorize.anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//        The below code is how the http using the Customizer for more read the official document.......
//        Customizer<CsrfConfigurer<HttpSecurity>> customizer = new Customizer<CsrfConfigurer<HttpSecurity>>() {
//            @Override
//            public void customize(CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer) {
//                httpSecurityCsrfConfigurer.disable();
//            }
//        };
//
//        http.csrf(customizer);
//
//        Customizer<HttpBasicConfigurer<HttpSecurity>> configurerCustomizer = new Customizer<HttpBasicConfigurer<HttpSecurity>>() {
//            @Override
//            public void customize(HttpBasicConfigurer<HttpSecurity> httpSecurityHttpBasicConfigurer) {
//                httpSecurityHttpBasicConfigurer.disable();
//            }
//        };
//
//        http.httpBasic(configurerCustomizer);

        return http.build();
    }
}
