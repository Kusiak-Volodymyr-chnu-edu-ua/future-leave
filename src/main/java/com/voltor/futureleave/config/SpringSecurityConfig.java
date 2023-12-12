package com.voltor.futureleave.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

import com.voltor.futureleave.api.v1.ApiConstants;
import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.security.jwt.JwtService;
import com.voltor.futureleave.security.jwt.JwtTokenAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {

	protected static final String MATCHES_ALL_PATHS = "/**";
	protected static final String EXCLUDE_PATH = "/error";

	@Autowired
	protected JwtService jwtService;

	@Bean
	public SecurityFilterChain filterChain( HttpSecurity http ) throws Exception {
		
		http.sessionManagement(sessionManagementConfig -> 
			sessionManagementConfig.sessionCreationPolicy( SessionCreationPolicy.NEVER ));

		http.cors( corsConf -> corsConf.disable() );
		http.csrf( csrfConf -> csrfConf.disable() );
		http.addFilterAfter( jwtTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class );
		http.httpBasic( httpBasicConf -> httpBasicConf.authenticationEntryPoint( new CustomAuthenticationEntryPoint() ) );
		http.headers( headersConf -> headersConf.frameOptions( foConf -> foConf.sameOrigin().disable() ) );

		http.authorizeHttpRequests(authorize -> 
			authorize
				.requestMatchers( 
						AntPathRequestMatcher.antMatcher( ApiConstants.API_PREFIX + ApiConstants.AUTHENTICATION_ENDPOINT ),
						AntPathRequestMatcher.antMatcher( ApiConstants.API_PREFIX + ApiConstants.REFRESH_TOKEN_ENDPOINT ),
						AntPathRequestMatcher.antMatcher( "/info" ),
						AntPathRequestMatcher.antMatcher( "/**" ) )
				.permitAll()
				.requestMatchers( CorsUtils::isPreFlightRequest ).permitAll()
				.requestMatchers( AntPathRequestMatcher.antMatcher( ApiConstants.API_PREFIX + MATCHES_ALL_PATHS ) )
					.hasAnyRole( 
							Role.ROOT.name(), 
							Role.SESSION_USER.name() ) );
		return http.build();
	}

	private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter() {
		return new JwtTokenAuthenticationFilter( ApiConstants.API_PREFIX + MATCHES_ALL_PATHS, EXCLUDE_PATH,
				jwtService );
	}

	@Bean
	public AuthenticationManager authenticationManager( AuthenticationConfiguration authenticationConfiguration )
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
