package com.example.gateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class RoleFilterProvider {

    private final ReactiveJwtDecoder jwtDecoder;

    @Autowired
    public RoleFilterProvider(ReactiveJwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    public GatewayFilter byRole(String role) {
        return new RoleBasedAuthorizationFilter(role, jwtDecoder);
    }
}
