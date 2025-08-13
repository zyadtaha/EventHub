package com.eventhub.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import static com.eventhub.constant.OpenApiConstant.*;

@OpenAPIDefinition(
        info = @Info(
                title = TITLE,
                version = VERSION,
                description = DESCRIPTION
        ),
        security = {
                @SecurityRequirement(name = SECURITY_SCHEME_NAME)
        }
)
@SecurityScheme(
        name = SECURITY_SCHEME_NAME,
        type = SecuritySchemeType.HTTP,
        scheme = SECURITY_SCHEME_SCHEME,
        description = SECURITY_SCHEME_DESCRIPTION,
        bearerFormat = BEARER_FORMAT,
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}

