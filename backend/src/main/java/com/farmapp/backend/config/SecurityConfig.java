package com.farmapp.backend.config;

import com.farmapp.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // we use JWT, so disable CSRF and make the session stateless
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // auth endpoints are public
                        .requestMatchers("/api/auth/**").permitAll()
                        // public product listing & get
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        // require authentication for create/update/delete and my-products
                        .requestMatchers(HttpMethod.POST, "/api/products").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/products/my").authenticated()
                        // any other request needs authentication
                        .anyRequest().authenticated()
                )
                // optional: keep http basic for quick testing, remove if you want JWT only
                .httpBasic(withDefaults());

        // add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
