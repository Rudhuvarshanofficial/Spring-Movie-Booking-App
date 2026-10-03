package com.rudhuvarshan.MovieBooking.configuration;

import com.rudhuvarshan.MovieBooking.model.Users;
import com.rudhuvarshan.MovieBooking.repository.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(customizer -> customizer.disable())

                .authorizeHttpRequests(request -> request
                        .requestMatchers("/user/register").permitAll()
                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint((request, response, authException) -> {

                            response.setStatus(401);
                            response.setContentType("application/json");

                            response.getWriter().write("""
                                    {
                                        "status": 401,
                                        "error": "Unauthorized",
                                        "message": "Authentication is required to access this resource",
                                        "path": "%s"
                                    }
                                    """.formatted(request.getRequestURI()));
                        })

                        .accessDeniedHandler((request, response, accessDeniedException) -> {

                            response.setStatus(403);
                            response.setContentType("application/json");

                            response.getWriter().write("""
                                    {
                                        "status": 403,
                                        "error": "Forbidden",
                                        "message": "You do not have permission to access this resource",
                                        "path": "%s"
                                    }
                                    """.formatted(request.getRequestURI()));
                        })
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider dao =
                new DaoAuthenticationProvider(userDetailsService);

        dao.setPasswordEncoder(new BCryptPasswordEncoder(12));

        return dao;
    }

    @Bean
    CommandLineRunner createAdmin(UsersDao usersDao) {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder(12);

        return args -> {

            if (usersDao.findByEmail("admin@gmail.com") == null) {

                Users admin = new Users(
                        "admin@gmail.com",
                        encoder.encode("123"),
                        "ADMIN"
                );

                usersDao.save(admin);
            }
        };
    }
}