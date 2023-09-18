package com.voltor.futureleave.config;

import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

@Configuration
@EnableRetry
public class WebConfig implements WebMvcConfigurer {

	private static boolean productionMode;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${org.sample.web.enable_production_mode}")
	private Boolean enableProductionMode;

	@Override
	public void configureMessageConverters( List< HttpMessageConverter< ? > > converters ) {
		// https://www.baeldung.com/jackson-optional
		// https://gdpotter.com/2017/05/24/custom-spring-mvc-jackson/
		objectMapper.registerModule( new Jdk8Module() );

		WebMvcConfigurer.super.configureMessageConverters( converters );
	}

	@PostConstruct
	private void init() {
		setProductionMode( enableProductionMode );
	}

	@Override
	public void addCorsMappings( final CorsRegistry registry ) {
		registry.addMapping( "/**" ).allowedMethods( "GET", "POST", "PUT", "DELETE", "PATCH" );
	}

	@Override
	public void configureContentNegotiation( ContentNegotiationConfigurer configurer ) {
		configurer.defaultContentType( MediaType.APPLICATION_JSON );
	}

	@Override
	public void addResourceHandlers( ResourceHandlerRegistry registry ) {
		registry.addResourceHandler( "swagger-ui.html" ).addResourceLocations( "classpath:/META-INF/resources/" );
		registry.addResourceHandler( "/webjars/**" ).addResourceLocations( "classpath:/META-INF/resources/webjars/" );
	}
	
	@Override
	public void addFormatters( FormatterRegistry registry ) {
		registry.addConverter( new StringToLocalDateTimeConverter() );
	}

	private static void setProductionMode( boolean productionMode ) {
		WebConfig.productionMode = productionMode;
	}

	public static boolean isProductionMode() {
		return productionMode;
	}
	
}
