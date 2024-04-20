package com.central.parkapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(
                new io.swagger.v3.oas.models.info.Info()
                        .title("Rest Api - Spring Park")
                        .description("Rest Api for administration of a park")
                        .version("v1.0.0")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new io.swagger.v3.oas.models.info.License().name("Apache 2.0").url("http://springdoc.org"))
                        .contact(new Contact().name("Pedro Paulo").email("contatos.pedrosilva@gmail.com")
                        )
        );
    }

}
