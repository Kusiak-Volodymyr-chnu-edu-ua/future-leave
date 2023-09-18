package com.voltor.futureleave.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import jakarta.validation.MessageInterpolator;
import jakarta.validation.Validation;

/**
 * Configure javax.validation to always interpolate messages to English.
 */
@Configuration
public class ValidatorConfig {

	@Bean
	public LocalValidatorFactoryBean getValidator() {
		final LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setMessageInterpolator(new AlwaysEnglishMessageInterpolator());
		return bean;
	}
	
	/**
	 * This bean is necessary for @PathVariable and @RequestParam validation in
	 * REST controllers
	 */
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
		postProcessor.setProxyTargetClass(true);
	    return postProcessor;
	}

	private final static class AlwaysEnglishMessageInterpolator implements MessageInterpolator {
		private final MessageInterpolator defaultInterpolator
				= Validation.byDefaultProvider().configure().getDefaultMessageInterpolator();

		@Override
		public String interpolate(String messageTemplate, Context context) {
			return defaultInterpolator.interpolate(messageTemplate, context, Locale.ENGLISH);
		}

		@Override
		public String interpolate(String messageTemplate, Context context, Locale locale) {
			return defaultInterpolator.interpolate(messageTemplate, context, Locale.ENGLISH);
		}
	}
}
