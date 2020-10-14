package com.foobar.accountservice;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        OAuthFlow oAuthFlow = new OAuthFlow();

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("OAuth",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("jwt")
                                .in(SecurityScheme.In.HEADER).name("Authorization")
                                .flows(new OAuthFlows().implicit(oAuthFlow))))
                .addSecurityItem(new SecurityRequirement().addList("OAuth"))

                .info(new Info()
                        .title("JSON Web Token Authentication API")//
                        .description("This is a sample JWT authentication service. You can find out more about JWT at [https://jwt.io/](https://jwt.io/). For this sample, you can use the `admin` or `client` users (password: admin and client respectively) to test the authorization filters. Once you have successfully logged in and obtained the token, you should click on the right top button `Authorize` and introduce it with the prefix \"Bearer \".")//
                        .version("1.0.0")//
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
