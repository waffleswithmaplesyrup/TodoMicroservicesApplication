package com.waffles.auth_microservice.Security;

import com.waffles.auth_microservice.Security.Token.JwtFilter;
import com.waffles.auth_microservice.Security.UserDetails.UserDetailsServiceImpl;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(
            UserDetailsServiceImpl userDetailsService,
            JwtFilter jwtFilter
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                /// disable the function that blocks POST, PUT, DELETE requests
                .csrf(csrf -> csrf.disable())
                /// requires the user to be authenticated before accessing the endpoint
                .authorizeHttpRequests(authorize -> authorize
                        // AuthController handles logging in and registering,
                        // so it's accessible to public
                        .requestMatchers("/auth/**").permitAll()
                        // TODO !! I am permitting all for now
//                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                /// For web browser: displays a form to authenticate the user to access the endpoints
//                .formLogin(Customizer.withDefaults())
                /// For Postman: instead of login form,
                /// allows the user to be authenticated using basic auth
                .httpBasic(Customizer.withDefaults())
                /// STATELESS changes the session id after every request
                /// so when you log in using formLogin, that is one request done
                /// a new session id is generated for the next request so you need to log in again
                /// because of this, FormLogin doesn't work anymore
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                /// add a filter that checks for bearer token
                /// and if the user is authorized to access the endpoint
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /// This is how we authenticate user during login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /// To config the Authentication Provider so that it takes user information from our database
    /// ** Make sure UserDetails and UserDetailsService are implemented **
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
}
