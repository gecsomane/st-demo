package com.startrac.task_management_system.config;

import com.startrac.task_management_system.service.JwtService;
import com.startrac.task_management_system.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JWTAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String requestAuthHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (Objects.nonNull(requestAuthHeader) && requestAuthHeader.startsWith("Bearer ")) {
            jwt = requestAuthHeader.substring(7);
            try {
                username = jwtService.extractUserName(jwt);
            } catch (Exception e) {
                System.out.println("Username extraction failed!");
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(username) && Objects.isNull(authentication)) {
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);

            if (jwtService.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
