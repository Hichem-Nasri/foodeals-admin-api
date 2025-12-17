package net.foodeals.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${upload.directory}")
	private String uploadDir;

	@Value("${user.dir}")
	private String usrDir;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**") // URL prefix
		 .addResourceLocations("file:" + usrDir.replace("\\", "/") + "/" + uploadDir + "/");
	}
	
	
	@Bean
	public MultipartConfigElement multipartConfigElement() {
	    MultipartConfigFactory factory = new MultipartConfigFactory();
	    factory.setMaxFileSize(DataSize.ofMegabytes(1000));
	    factory.setMaxRequestSize(DataSize.ofMegabytes(1000));
	    return factory.createMultipartConfig();
	}
}