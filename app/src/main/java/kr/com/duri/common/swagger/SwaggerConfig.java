package kr.com.duri.common.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${server.protocol}")
    String protocol;

    @Value("${server.host}")
    String host;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .addServersItem(new Server().url(protocol + "://" + host).description("https"))
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info().title("Duri-Salon").description("Swaggey UI").version("1.0.1");
    }
}
