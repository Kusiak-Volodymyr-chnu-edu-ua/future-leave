package com.voltor.futureleave.config;

import java.util.Comparator;

import org.junit.jupiter.api.ClassDescriptor;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.ClassOrdererContext;
import org.springframework.boot.test.context.SpringBootTest;

public class SpringBootTestClassOrderer implements ClassOrderer {
	
	@Override
	public void orderClasses( ClassOrdererContext classOrdererContext ) {
		classOrdererContext.getClassDescriptors()
				.sort( Comparator.comparingInt( SpringBootTestClassOrderer::getOrder ) );
	}

	private static int getOrder( ClassDescriptor classDescriptor ) {
		if ( classDescriptor.findAnnotation( SpringBootTest.class ).isPresent() ) {
			return 4;
		}
		return 1;
	}
}
