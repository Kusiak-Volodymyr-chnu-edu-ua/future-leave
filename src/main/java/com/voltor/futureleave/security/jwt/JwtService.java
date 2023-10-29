package com.voltor.futureleave.security.jwt;

import java.util.Date;
import java.util.UUID;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.voltor.futureleave.security.UserDetailsServiceImpl;
import com.voltor.futureleave.security.jwt.exception.JwtBadSignatureException;
import com.voltor.futureleave.security.jwt.exception.JwtExpirationException;
import com.voltor.futureleave.security.jwt.exception.MalformedJwtException;
import com.voltor.futureleave.service.AuthenticatedUserService;
import com.voltor.futureleave.service.DateTimeService;
import com.voltor.futureleave.service.RefreshTokenService;

@Service
public class JwtService {

	private static final Logger LOGGER = LoggerFactory.getLogger( JwtService.class );

	@Value("${org.sample.web.jwt.jwtKey}")
	private String jwtKey;

	@Value("${org.sample.web.jwt.accessTokenExpirationInMinutes}")
	private Integer accessTokenExpirationInMinutes;

	@Value("${org.sample.web.jwt.refreshTokenExpirationInMinutes}")
	private Integer refreshTokenExpirationInMinutes;

	private Algorithm algorithm;
	private RefreshTokenService refreshTokenService;
	private UserDetailsServiceImpl userDetailsService;
	private AuthenticatedUserService userAuthorizationService;

	@Autowired
	public JwtService( AuthenticatedUserService userAuthorizationService, RefreshTokenService refreshTokenService, UserDetailsServiceImpl userDetailsService ) {
		this.userAuthorizationService = userAuthorizationService;
		this.refreshTokenService = refreshTokenService;
		this.userDetailsService = userDetailsService;
	}

	@PostConstruct
	public void init() throws IllegalArgumentException {
		try {
			algorithm = Algorithm.HMAC256( jwtKey );
		} catch ( IllegalArgumentException e ) {
			LOGGER.error( "Algorithm could not be initialized", e );
			throw e;
		}
	}

	public String generateAccessToken() {
		return generateToken( userAuthorizationService.getCurrentUser().getLogin(),
				expirationDate( accessTokenExpirationInMinutes ) );
	}

	public String generateRefreshToken() {
		Date expirationDate = expirationDate( refreshTokenExpirationInMinutes );
		String refreshToken = generateToken( UUID.randomUUID().toString(), expirationDate );
		refreshTokenService.create( refreshToken, DateTimeService.toZonedDateTime( expirationDate ) );
		return refreshToken;
	}

	private String generateToken( String subject, Date expirationDate ) {
		return JWT.create()
				.withIssuer( "Web" )
				.withSubject( subject )
				.withIssuedAt( currentDate() )
				.withExpiresAt( expirationDate )
				.sign( algorithm );
	}

	private Date currentDate() {
		return new Date( System.currentTimeMillis() );
	}

	private Date expirationDate( int expirationInMinutes ) {
		return new Date( System.currentTimeMillis() + expirationInMinutes * 1000L * 60 );
	}

	public void verifyToken( String jwt ) {
		JWTVerifier verifier = JWT.require( algorithm ).build();
		try {
			verifier.verify( jwt );
		} catch ( TokenExpiredException e ) {
			throw new JwtExpirationException( "Token has expired" );
		} catch ( SignatureVerificationException e ) {
			throw new JwtBadSignatureException( "Signature is not valid" );
		} catch ( JWTVerificationException ex ) {
			throw new MalformedJwtException( "Token was not a valid JWT" );
		}
	}

	public UserDetails getUserDetailsByJwt( String jwtKey ) {
		String subject = getSubject( jwtKey );
		return userDetailsService.loadUserByUsername( subject );
	}

	public String getSubject( String jwt ) {
		DecodedJWT decodedJWT = JWT.decode( jwt );
		return decodedJWT.getSubject();
	}

	public Integer getAccessTokenExpirationInMinutes() {
		return accessTokenExpirationInMinutes;
	}

	public Integer getRefreshTokenExpirationInMinutes() {
		return refreshTokenExpirationInMinutes;
	}

}
