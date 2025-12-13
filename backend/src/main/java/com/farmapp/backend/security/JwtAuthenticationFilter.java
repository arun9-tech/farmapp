package com.farmapp.backend.security;

import com.farmapp.backend.model.Farmer;
import com.farmapp.backend.repository.FarmerRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final FarmerRepository farmerRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, FarmerRepository farmerRepository) {
        this.jwtUtil = jwtUtil;
        this.farmerRepository = farmerRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String header = request.getHeader("Authorization");
            if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = header.substring(7).trim();
            if (!jwtUtil.validateToken(token)) {
                log.debug("JWT validation failed for token: {}", token);
                filterChain.doFilter(request, response);
                return;
            }

            String subject = jwtUtil.getSubject(token); // expected to be farmer id as string
            Long farmerId;
            try {
                farmerId = Long.parseLong(subject);
            } catch (NumberFormatException ex) {
                log.debug("JWT subject is not a valid id: {}", subject);
                filterChain.doFilter(request, response);
                return;
            }

            Farmer farmer = farmerRepository.findById(farmerId).orElse(null);
            if (farmer == null) {
                log.debug("No farmer found for id from JWT: {}", farmerId);
                filterChain.doFilter(request, response);
                return;
            }

            // Build Authentication with principal = farmer id string so Principal.getName() returns id
            // If you have roles/authorities on Farmer, map them into the authorities list here.
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    subject,
                    null,
                    Collections.emptyList()
            );
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception ex) {
            // Never fail the request pipeline with a server exception from the filter; just log and continue.
            log.error("Error during JWT authentication filter", ex);
        }

        filterChain.doFilter(request, response);
    }
}
