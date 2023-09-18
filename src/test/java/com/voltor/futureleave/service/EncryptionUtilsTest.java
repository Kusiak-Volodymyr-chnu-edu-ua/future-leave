package com.voltor.futureleave.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class EncryptionUtilsTest {

	// Passwords here are not encrypted, just some weird passwords
	private static final String PASSWORD = "58aRPhtKGWWVoTkbMDfLtfy2gIai7f";
	private static final String SECOND_PASSWORD = "uYti1w7OEF3ouM8nk9ys";

	@Test
	void sameStringMD5HasheEqualityTest() {
		assertEquals( EncryptingUtils.encodeWithMD5( PASSWORD ), EncryptingUtils.encodeWithMD5( PASSWORD ) );
	}

	@Test
	void differentStringsMD5HashesNonEqualityTest() {
		assertNotEquals( EncryptingUtils.encodeWithMD5( PASSWORD ), EncryptingUtils.encodeWithMD5( SECOND_PASSWORD ) );
	}

	@Test
	void knownPasswordCorrectBCryptHashTest() {
		String hash = EncryptingUtils.encodeWithBCrypt( PASSWORD );
		assertTrue( BCrypt.checkpw( PASSWORD, hash ) );
	}

}