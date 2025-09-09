package com.itgirl.account_service_a.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(HeaderAuthenticationFilter.class);

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String ROLES_HEADER = "X-Roles"; // ADMIN,USER

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String userIdHeader = request.getHeader(USER_ID_HEADER);
        String rolesHeader = request.getHeader(ROLES_HEADER);

        logger.info("HeaderAuthenticationFilter: received X-User-Id={} X-Roles={}", userIdHeader, rolesHeader);

        if (userIdHeader != null && !userIdHeader.isEmpty()) {
            try {
                Long userId = Long.parseLong(userIdHeader);

                List<SimpleGrantedAuthority> authorities = List.of();
                if (rolesHeader != null && !rolesHeader.isEmpty()) {
                    authorities = Arrays.stream(rolesHeader.split(","))
                            .map(String::trim)
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList());
                }

                // Если нет ролей, добавляем ROLE_USER по умолчанию
                if (authorities.isEmpty()) {
                    authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
                }

                // Используем конструктор с authorities — Spring автоматически ставит authenticated = true
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(auth);

                logger.info("HeaderAuthenticationFilter: Authentication set successfully: {}", auth);

            } catch (NumberFormatException e) {
                logger.warn("Invalid X-User-Id header: {}", userIdHeader, e);
            }
        } else {
            logger.info("HeaderAuthenticationFilter: X-User-Id header missing");
        }

        filterChain.doFilter(request, response);
    }
}
