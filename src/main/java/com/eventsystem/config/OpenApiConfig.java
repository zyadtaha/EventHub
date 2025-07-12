package com.eventsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "EventHub API",
                version = "1.0.0",
                description = "API for the EventHub system."
        ),
        servers = @Server(
                description = "Local development server",
                url = "http://localhost:8080"
        ),
        security = {
                @SecurityRequirement(name = "BearerAuth")
        }
)
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        description = "Use the JWT token obtained from the authentication server to access protected resources.",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}

