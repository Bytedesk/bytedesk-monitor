package com.bytedesk.monitor;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String adminContextPath;

    public SecurityConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler =
                new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        http
            .authorizeHttpRequests(authorize -> authorize
                // 放行静态资源
                .requestMatchers(
                    adminContextPath + "/assets/**",
                    adminContextPath + "/variables.css",
                    adminContextPath + "/login"
                ).permitAll()
                // 其余所有请求需要认证
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage(adminContextPath + "/login")
                .successHandler(successHandler)
            )
            .logout(logout -> logout
                .logoutUrl(adminContextPath + "/logout")
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers(
                    adminContextPath + "/instances",
                    adminContextPath + "/actuator/**"
                )
            );

        return http.build();
    }
}
