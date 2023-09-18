package com.voltor.futureleave.security.jwt;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import com.voltor.futureleave.security.jwt.exception.JwtBadSignatureException;
import com.voltor.futureleave.security.jwt.exception.JwtExpirationException;
import com.voltor.futureleave.security.jwt.exception.MalformedJwtException;

public class JwtTokenAuthenticationFilter extends GenericFilterBean {

	private static final Logger LOGGER = LoggerFactory.getLogger( JwtTokenAuthenticationFilter.class );

	private final RequestMatcher requestMatcher;
	private final RequestMatcher excludeMatcher;
	private final JwtService jwtService;

	public JwtTokenAuthenticationFilter( String path, String excludePath, JwtService jwtService ) {
		this.jwtService = jwtService;
		this.requestMatcher = new AntPathRequestMatcher( path );
		this.excludeMatcher = new AntPathRequestMatcher( excludePath );
	}

	@Override
	public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain )
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if ( !requiresAuthentication( request ) ) {
			/*
			 * if the URL requested doesn't match the URL handled by the filter,
			 * then we chain to the next filters.
			 */
			chain.doFilter( request, response );
			return;
		}

		String header = request.getHeader( AUTHORIZATION );
		if ( header == null || !header.startsWith( "Bearer " ) ) {
			/*
			 * If there's not authentication information, then we chain to the
			 * next filters. The SecurityContext will be analyzed by the chained
			 * filter that will throw AuthenticationExceptions if necessary
			 */
			chain.doFilter( request, response );
			return;
		}

		String jwt = null;

		try {
			/*
			 * The token is extracted from the header or parameter. It's then
			 * checked (signature and expiration) An Authentication is then
			 * created and registered in the SecurityContext. The
			 * SecurityContext will be analyzed by chained filters that will
			 * throw Exceptions if necessary (like if authorizations are
			 * incorrect).
			 */
			jwt = extractJwt( request );
			jwtService.verifyToken( jwt );
			Authentication auth = buildAuthenticationFromJwt( jwt, request );
			SecurityContextHolder.getContext().setAuthentication( auth );

			chain.doFilter( request, response );

		} catch ( JwtExpirationException ex ) {
			LOGGER.warn( "Token has expired {}", jwt );
			response.sendError( 426 );
		} catch ( JwtBadSignatureException | MalformedJwtException ex ) {
			LOGGER.warn( "Token is malformed {}", jwt );
			throw new AccessDeniedException( jwt, ex );
		}

		/* SecurityContext is then cleared since we are stateless. */
		SecurityContextHolder.clearContext();
	}

	private boolean requiresAuthentication( HttpServletRequest request ) {
		if ( excludeMatcher.matches( request ) )
			return false;

		return requestMatcher.matches( request );
	}

	private String extractJwt( HttpServletRequest request ) {
		String authHeader = request.getHeader( AUTHORIZATION );
		return authHeader.substring( "Bearer ".length() );
	}

	private Authentication buildAuthenticationFromJwt( String jwt, HttpServletRequest request ) {
		UserDetails userDetails = jwtService.getUserDetailsByJwt( jwt );
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( userDetails,
				jwtService.getSubject( jwt ), userDetails.getAuthorities() );

		authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
		return authentication;
	}

}
