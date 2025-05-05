package com.waffles.auth_microservice.Security.Token;

import com.waffles.auth_microservice.Security.UserDetails.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final ApplicationContext context;

    @Autowired
    public JwtFilter(
            TokenService tokenService,
            ApplicationContext context
    ) {
        this.tokenService = tokenService;
        this.context = context;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        /// retrieve bearer token from request header
        String authHeader = request.getHeader("Authorization");

        String bearerToken = null;
        String email = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            bearerToken = authHeader.substring(7);
            ///  decode the logged in user's email from bearer token
            email = tokenService.decodeToken(bearerToken);
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            /// Application context takes the given email and load the user details by email
            UserDetails userDetails = context.getBean(UserDetailsServiceImpl.class).loadUserByUsername(email);

            if(tokenService.validateToken(bearerToken)) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }
        filterChain.doFilter(request, response);

    }
}
