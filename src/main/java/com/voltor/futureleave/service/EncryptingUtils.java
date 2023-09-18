package com.voltor.futureleave.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.bcrypt.BCrypt;

public final class EncryptingUtils {

	private EncryptingUtils() {
	}
	
	/**  
	 * Encodes a string using first MD5 and then BCrypt algorithms
	*/   
	public static String crypt(String str) {   
	  
		if ( Strings.isEmpty( str ) ) {
			return "";
			// commented because of autologin
			// throw new IllegalArgumentException("String to encript cannot be
			// null or zero length");
		}  
	  
		return encodeWithBCrypt( encodeWithMD5( str ) );
	}

	public static String encodeWithMD5( String str ) {
	   StringBuilder hexString = new StringBuilder();   
	   try {   
	     MessageDigest md = MessageDigest.getInstance("MD5");   
	     md.update( str.getBytes() );   
	     byte[] hash = md.digest();   
	     
	     for (int i = 0; i < hash.length; i++) {   
	       if ((0xff & hash[i]) < 0x10) {   
	         hexString.append("0" + Integer.toHexString((0xFF & hash[i])));   
	       } else {   
	         hexString.append(Integer.toHexString(0xFF & hash[i]));   
	       }   
	     }   
	   } catch (NoSuchAlgorithmException e) {   
	     throw new IllegalStateException( "Failed to encode string: " + str, e );   
	   }   
	  
	   return hexString.toString();   
	}   
	
	public static String encodeWithBCrypt( String str ) {
		return BCrypt.hashpw( str, BCrypt.gensalt() );
	}
	
	/**
	 * This method checks that a plain text password matches a previously hashed
	 */
	public static boolean checkPassword( String rawPass, String hashed ) {
		return BCrypt.checkpw( encodeWithMD5( rawPass ), hashed );
	}

}
