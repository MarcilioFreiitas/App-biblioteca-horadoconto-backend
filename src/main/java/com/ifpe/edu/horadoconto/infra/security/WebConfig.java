package com.ifpe.edu.horadoconto.infra.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    // Servindo as imagens
	    registry.addResourceHandler("/imagens/capas/**")
	            .addResourceLocations("file:/usr/share/app/imagens/capas/");
	

        // Servindo o arquivo de redefinição de senha
        registry.addResourceHandler("/password-reset.html")
                .addResourceLocations("classpath:/static/");
    }
}
