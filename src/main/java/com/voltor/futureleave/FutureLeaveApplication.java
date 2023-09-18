package com.voltor.futureleave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackageClasses = {FutureLeaveApplication.class, Jsr310JpaConverters.class})
@EnableAutoConfiguration(exclude = RepositoryRestMvcAutoConfiguration.class)
@EnableSpringDataWebSupport
@EnableScheduling
@EnableCaching
public class FutureLeaveApplication extends SpringBootServletInitializer {

	public static void main(final String[] args) {
		SpringApplication.run(FutureLeaveApplication.class, args);
	}

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FutureLeaveApplication.class);
    }
 
}
