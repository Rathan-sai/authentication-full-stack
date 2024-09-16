package com.tech.programming.Authentication_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize-> authorize
                        .requestMatchers("register", "login")
                        .permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        ;

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

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //To define a customize own username and password..... and how it works.
//    @Bean
//    public UserDetailsService userDetailsService(){
//
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()
//                .username("rathan")
//                .password("@1324")
//                .roles("USER")
//                .build();
//
//        UserDetails user2 = User
//                .withDefaultPasswordEncoder()
//                .username("jeevan")
//                .password("@9485")
//                .roles("ADMIN")
//                .build();
//
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }
}
