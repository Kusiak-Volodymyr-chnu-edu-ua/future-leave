package com.voltor.futureleave.model;

public enum SupportedTimeZone {
	BRUSSELS("Europe/Brussels"),
	AMSTERDAM("Europe/Amsterdam"),
	BERLIN("Europe/Berlin"),
	MADRID("Europe/Madrid");
	
	private final String name;

	private SupportedTimeZone( String name ) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static SupportedTimeZone getValueByName( String name ) {
		for ( SupportedTimeZone timeZone : SupportedTimeZone.values() ) {
			if ( timeZone.name.equals( name ) ) {
				return timeZone;
			}
		}
		return getDefault();
	}
	
	private static SupportedTimeZone getDefault() {
		return AMSTERDAM;
	}
	
	public static String getDefaultName() {
		return getDefault().name;
	}
}
