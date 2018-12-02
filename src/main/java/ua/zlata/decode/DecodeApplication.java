package ua.zlata.decode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import ua.zlata.decode.argument.resolver.OperationsAnnotationHandlerMethodArgumentResolver;

@SpringBootApplication
@EnableWebFlux
public class DecodeApplication implements WebFluxConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(DecodeApplication.class, args);
    }

    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(new OperationsAnnotationHandlerMethodArgumentResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://zlata-decode.herokuapp.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .maxAge(3600);
    }
}
