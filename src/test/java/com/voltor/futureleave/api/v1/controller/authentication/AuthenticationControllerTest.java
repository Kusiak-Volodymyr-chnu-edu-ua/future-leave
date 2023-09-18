package com.voltor.futureleave.api.v1.controller.authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.voltor.futureleave.api.v1.authentication.AuthenticationController;
import com.voltor.futureleave.api.v1.authentication.AuthenticationResponse;
import com.voltor.futureleave.api.v1.exception.ExceptionsHandler;
import com.voltor.futureleave.builder.AuthDataBuilder;
import com.voltor.futureleave.builder.AuthenticatedUserBuilder;
import com.voltor.futureleave.config.SpringSecurityConfig;
import com.voltor.futureleave.model.AuthData;
import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.security.AuthenticatedUser;
import com.voltor.futureleave.security.UserDetailsServiceImpl;
import com.voltor.futureleave.security.jwt.JwtService;
import com.voltor.futureleave.security.jwt.exception.JwtBadSignatureException;
import com.voltor.futureleave.security.jwt.exception.JwtExpirationException;
import com.voltor.futureleave.security.jwt.exception.MalformedJwtException;
import com.voltor.futureleave.service.AuthenticatedUserService;
import com.voltor.futureleave.service.RefreshTokenService;

@WebMvcTest( AuthenticationController.class )
@ActiveProfiles(profiles = { "develop" })
@Import( SpringSecurityConfig.class  )
public class AuthenticationControllerTest {

	private static final String API_URL = AuthenticationController.API_URL;

	private HttpMessageConverter< Object > mappingJackson2HttpMessageConverter;

	@MockBean
	private JwtService jwtService;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private UserDetailsServiceImpl userDetailsService;

	@MockBean
	private RefreshTokenService refreshTokenService;

	@MockBean
	private AuthenticatedUserService userAuthorizationService;

	@Autowired
	private AuthenticationController authenticationController;

	protected MockMvc mvc;

	@Autowired
	protected void setConverters( HttpMessageConverter< Object >[] converters ) {
		this.mappingJackson2HttpMessageConverter = Stream.of( converters )
				.filter( hmc -> hmc instanceof MappingJackson2HttpMessageConverter ).findAny().get();
	}

	@BeforeEach
	public void setup() {
		this.mvc = MockMvcBuilders.standaloneSetup( authenticationController )
				.setControllerAdvice( new ExceptionsHandler() ).build();
	}

	@Test
	public void loginWithWrongCredentialsTest() throws Exception {
		given( authenticationManager.authenticate( any() ) ).willThrow( BadCredentialsException.class );
		this.mvc.perform( post( API_URL + "/login" )
    				.contentType( MediaType.APPLICATION_JSON )
    				.accept( MediaType.APPLICATION_JSON )
    				.content( "{\"clientId\": \"gebruiker\", \"clientSecret\": \"wachtwoord\"}" ) )
				.andExpect( status().isUnauthorized() );
	}
	
	@Test
	public void authenticationProcessTest() throws Exception {
		Authentication authentication = new UsernamePasswordAuthenticationToken( "gebruiker", "wachtwoord");
		given( authenticationManager.authenticate( any() ) ).willReturn( authentication );
		
		this.mvc.perform( post( API_URL + "/login" )
    				.contentType( MediaType.APPLICATION_JSON )
    				.accept( MediaType.APPLICATION_JSON )
    				.content( "{\"clientId\": \"gebruiker\", \"clientSecret\": \"wachtwoord\"}" ) )
				.andExpect( status().isOk() );
		
		verify( authenticationManager, times( 1 ) ).authenticate( authentication );
		verify( userAuthorizationService, times( 1 ) ).authenticateUser( authentication );
	}

	@Test
	public void shouldReturnToken() throws Exception {
		AuthenticatedUser authenticatedUser = AuthenticatedUserBuilder.start().build();
		given( authenticationManager.authenticate( any() ) )
				.willReturn( new UsernamePasswordAuthenticationToken( authenticatedUser.getClientId(), "bla" ) );

		AuthenticationResponse response = mockAuthenticationResponse();

		this.mvc.perform( post( API_URL + "/login" )
					.contentType( MediaType.APPLICATION_JSON )
    				.accept( MediaType.APPLICATION_JSON )
    				.content( "{\"clientId\": \"gebruiker\", \"clientSecret\": \"wachtwoord\"}" ) )
				.andExpect( status().isOk() )
				.andExpect( content().json( getJson( response ) ) );
	}

	@Test
	public void shouldRefreshToken() throws Exception {
		String refreshToken = "t63567otyj&&&&keneshterstan";

		AuthData authData = AuthDataBuilder.start().build(); 
		when( refreshTokenService.getTokenData( "someToken" ) ).thenReturn( authData );

		List< GrantedAuthority > authorityList = new ArrayList<>();
		authorityList.add( new SimpleGrantedAuthority( "ROLE_SUPPORT" ) );

		when( userDetailsService.authenticateUser( authData ) )
				.thenReturn( AuthenticatedUserBuilder.start().role( Role.SESSION_USER ).build() );

		AuthenticationResponse response = mockAuthenticationResponse();

		this.mvc.perform( post( API_URL + "/refresh" )
    				.contentType( MediaType.APPLICATION_JSON )
    				.param( "refreshToken", refreshToken )
    				.accept( MediaType.APPLICATION_JSON ) )
				.andExpect( status().isOk() )
				.andExpect( content().json( getJson( response ) ) );
	}

	private AuthenticationResponse mockAuthenticationResponse() {
		given( jwtService.generateAccessToken() ).willReturn( "testToken" );
		given( jwtService.generateRefreshToken() ).willReturn( "testRefreshToken" );
		given( jwtService.getAccessTokenExpirationInMinutes() ).willReturn( 5 );
		given( jwtService.getRefreshTokenExpirationInMinutes() ).willReturn( 8 );
		return new AuthenticationResponse( "testToken", "testRefreshToken", 5, 8 );
	}

	@Test
	public void shouldLogoutByToken() throws Exception {
		String token = "tokenedfs";
		this.mvc.perform( post( API_URL + "/logout" ).contentType( MediaType.APPLICATION_JSON )
				.param( "refreshToken", token ).accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() );

		verify( refreshTokenService ).delete( token );
	}

	@Test
	public void wrongRefreshTokenTest() throws Exception {
		doWrongRefreshTokenTest( new JwtExpirationException( "" ) );
		doWrongRefreshTokenTest( new JwtBadSignatureException( "" ) );
		doWrongRefreshTokenTest( new MalformedJwtException( "" ) );
	}

	public void doWrongRefreshTokenTest( RuntimeException jwtException ) throws Exception {
		String refreshToken = "t63567otyj&&&&keneshterstan";
		willThrow( jwtException ).willDoNothing().given( jwtService ).verifyToken( refreshToken );

		this.mvc.perform( post( API_URL + "/refresh" ).contentType( MediaType.APPLICATION_JSON ).param( "refreshToken",
				refreshToken ) ).andExpect( status().isUnauthorized() );

		verify( refreshTokenService, never() ).getTokenData( refreshToken );
	}

	@Test
	public void removeRefreshTokenOnTokenExpired() throws Exception {
		String refreshToken = "xcv";
		willThrow( new JwtExpirationException( "" ) ).willDoNothing().given( jwtService ).verifyToken( refreshToken );

		this.mvc.perform( post( API_URL + "/refresh" )
				.contentType( MediaType.APPLICATION_JSON )
				.param( "refreshToken", refreshToken ) )
		.andExpect( status().isUnauthorized() );

		verify( refreshTokenService, never() ).getTokenData( refreshToken );
		verify( refreshTokenService, times( 1 ) ).delete( refreshToken );
	}

	protected String getJson( Object object ) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write( object, MediaType.APPLICATION_JSON, mockHttpOutputMessage );
		return mockHttpOutputMessage.getBodyAsString();
	}
}
