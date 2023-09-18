package com.voltor.futureleave.model;

import java.util.Locale;

public enum SupportedLocale {
	EN("en", Locale.ENGLISH), 
	NL("nl", new Locale( "nl" ) ),  
	DE("de", Locale.GERMAN),  
	ES("es", new Locale( "es" ) );
	
	private final String name;
	private final Locale locale;

	private SupportedLocale(String name, Locale locale) {
		this.name = name;
		this.locale = locale;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static SupportedLocale getValueByName( String name ) {
		for ( SupportedLocale locale : SupportedLocale.values() ) {
			if ( locale.name.equals( name ) ) {
				return locale;
			}
		}
		return getDefault();
	}
	
	private static SupportedLocale getDefault() {
		return NL;
	}
	
	public static String getDefaultName() {
		return getDefault().name;
	}

	public Locale getLocale() {
		return locale;
	}
}
