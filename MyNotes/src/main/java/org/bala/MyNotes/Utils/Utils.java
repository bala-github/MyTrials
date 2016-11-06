package org.bala.MyNotes.Utils;

import java.nio.charset.StandardCharsets;

import org.bouncycastle.util.encoders.Base64;

public class Utils {

	public static String UrlSafeBase64encode(String plainText) {
		String encoded =  new String(Base64.encode(plainText.getBytes()), StandardCharsets.UTF_8);
		encoded = encoded.replace("/", "_");
		encoded = encoded.replace("+", "-");
		return encoded;
	}
	
	public static String UrlSafeBase64decode(String encoded) {
		encoded = encoded.replace("_", "/");
		encoded = encoded.replace("-", "+");
		return new String(Base64.decode(encoded),  StandardCharsets.UTF_8);
	}	
}
