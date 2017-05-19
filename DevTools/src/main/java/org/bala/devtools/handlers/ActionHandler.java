package org.bala.devtools.handlers;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.bala.ESQL.ESQuerySyntax;
import org.bala.ESQL.TokenValidationException;

public class ActionHandler {

	public static final String ENCODE_ACTION = "ENCODE";
	public static final String DECODE_ACTION = "DECODE";
	
	private Base64.Encoder base64Encoder = Base64.getEncoder();
	private Base64.Decoder base64Decoder = Base64.getDecoder();
	
	public String getElasticSearchQuery(String sql) throws TokenValidationException {
		
		ESQuerySyntax syntax = new ESQuerySyntax();
		
		return syntax.getQuery(syntax.validateQuery(sql));
	}
	
	public String encodeBase64(boolean isUrlSafe, String input) {
		
		String output;

		output = base64Encoder.encodeToString(input.getBytes(StandardCharsets.UTF_8)) ;

		if(isUrlSafe) {
			output = output.replaceAll("+", "-")
						.replaceAll("//", "_")
						.replaceAll("=", ".");
		}

		return output;
	}
	
	
	public String decodeBase64(boolean isUrlSafe, String input) {
		
		String output;

		if(isUrlSafe) {

			input = input.replaceAll("-", "+")
					.replaceAll("_", "//")
					.replaceAll(".", "=");

		}
			
		output = new String(base64Decoder.decode(input), StandardCharsets.UTF_8);

		return output;
	}	
}
