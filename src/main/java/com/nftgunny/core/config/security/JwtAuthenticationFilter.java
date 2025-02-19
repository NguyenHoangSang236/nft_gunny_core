package com.nftgunny.core.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nftgunny.core.config.constant.ConstantValue;
import com.nftgunny.core.config.constant.ResponseResult;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.entities.database.TokenInfo;
import com.nftgunny.core.utils.JwtUtils;
import com.nftgunny.core.utils.LoggingUtils;
import com.nftgunny.core.utils.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    final JwtUtils jwtUtils;
    final LoggingUtils loggingUtils;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final TokenInfo tokenInfo;

        try {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, PATCH, HEAD, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");

            // generate request ID
            String requestId = UUID.randomUUID().toString();
            request.setAttribute(ConstantValue.REQUEST_ID, requestId);
            log.info("Current Request ID: {}", request.getAttribute(ConstantValue.REQUEST_ID).toString());

            // if header is not null and starts with word 'Bearer' -> proceed filter
            if (authHeader != null && authHeader.startsWith("Bearer") && request.getRequestURI().contains("/authen/")) {
                // get jwt from header ('Bearer' length is 7 => get jwt after index 7 of header)
                jwt = jwtUtils.getJwtFromRequest(request);
                logger.info("Client jwt: " + jwt);
                boolean isJwtExpired = jwtUtils.isTokenExpired(jwt);

                // if jwt is expired, or user has not been authorized
                if (isJwtExpired ||
                        SecurityContextHolder.getContext() == null ||
                        SecurityContextHolder.getContext() instanceof AnonymousAuthenticationToken) {
                    logger.error("Client JWT expired");

                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("application/json");
                    response.getWriter().write(
                            convertObjectToJson(
                                    ApiResponse.builder()
                                            .result(ResponseResult.failed.name())
                                            .message("JWT has been expired")
                                            .build()
                            )
                    );

                    loggingUtils.logHttpServletRequest(request);
                    loggingUtils.logHttpServletResponse(
                            request,
                            response,
                            ApiResponse.builder()
                                    .result(ResponseResult.failed.name())
                                    .message("JWT has been expired")
                                    .build()
                    );

                    return;
                } else {
                    // get token info from jwt
                    tokenInfo = jwtUtils.getRefreshTokenInfoFromJwt(jwt);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            tokenInfo.getUserName(),
                            null,
                            tokenInfo.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    SecurityUtils.storeSecurityContext();
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            response.getWriter().write(
                    convertObjectToJson(ApiResponse.builder().result(ResponseResult.failed.name()).message(e.getMessage()).build())
            );
        }
    }


    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        } else {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        }
    }
}
