package com.nftgunny.core.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.nftgunny.core.config.logging.RequestGetLogger;
import com.nftgunny.core.config.meta_data.SystemConfigKeyName;
import com.nftgunny.core.config.constant.ResponseResult;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.utils.SystemConfigEnvUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    final JwtAuthenticationFilter jwtAuthenticationFilter;
    final SystemConfigEnvUtils systemConfigEnvUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        String[] patterns = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.SECURITY_IGNORED_REQUEST_MATCHERS).split(",");

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
//                .anonymous(AnonymousConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .logout(LogoutConfigurer::disable)
                .authorizeHttpRequests(
                        (request) -> request
                                .requestMatchers(patterns)
                                .authenticated()
                                .anyRequest().permitAll()
                )
                .sessionManagement(
                        sessionManagement -> {
                            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                        }
                )
//                .userDetailsService(userDetailsService)
//                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exception -> {
                            exception.authenticationEntryPoint(
                                    (request, response, authException) -> {
                                        if (authException != null) {
                                            ApiResponse apiResponse = new ApiResponse(
                                                    ResponseResult.failed.name(),
                                                    authException.getMessage(),
                                                    HttpStatus.UNAUTHORIZED
                                            );

                                            String jsonErrorResponse = new ObjectMapper().writeValueAsString(apiResponse);

                                            response.setContentType("application/json");
                                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                            response.getWriter().write(jsonErrorResponse);
                                        }
                                    }
                            );
                        }
                );

        return httpSecurity.build();
    }


//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        String[] patterns = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.SECURITY_IGNORED_REQUEST_MATCHERS).split(",");
//        return (web) -> web.ignoring().requestMatchers(patterns);
//    }


    @Bean
    public DefaultWebSecurityExpressionHandler expressionHandler() {
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setDefaultRolePrefix("");

        return handler;
    }


    @Bean
    public CookieSameSiteSupplier cookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofNone();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(ImmutableList.of("http://localhost:3000", "http://localhost:3001"));
        config.setAllowCredentials(true);
        config.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(ImmutableList.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Autowired
            RequestGetLogger requestLogger;

            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("http://localhost:3000", "http://localhost:3001")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .allowedMethods("*");
            }

            @Override
            public void addInterceptors(@NonNull InterceptorRegistry registry) {
                registry.addInterceptor(requestLogger);
            }
        };
    }


    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> oncePerRequestFilterFilterRegistrationBean() {
        FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthenticationFilter);
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
