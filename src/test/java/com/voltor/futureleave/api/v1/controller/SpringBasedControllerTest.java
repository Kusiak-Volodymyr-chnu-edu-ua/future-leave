package com.voltor.futureleave.api.v1.controller;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.voltor.futureleave.config.SpringSecurityConfig;
import com.voltor.futureleave.config.ValidatorConfig;
import com.voltor.futureleave.security.DummySecurityConfiguration;

@WithMockUser(roles = { "SESSION_USER" })
@ActiveProfiles(profiles = { "develop" })
@Import({ ValidatorConfig.class, DummySecurityConfiguration.class, SpringSecurityConfig.class,
		MappingJackson2HttpMessageConverter.class })
public abstract class SpringBasedControllerTest {

	private HttpMessageConverter<Object> mappingJackson2HttpMessageConverter;

	@Autowired
	protected MockMvc mvc;

	@Autowired
	protected void setConverters(HttpMessageConverter<Object>[] converters) {
		this.mappingJackson2HttpMessageConverter = Stream.of(converters)
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
				.findAny().get();
	}

	/* util */
	protected String getJson(Object object) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(object, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

}
