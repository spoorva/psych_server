package authentication;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestBase64 {
	
	

	public static void main(String[] args) {
		byte[] message = "NjkyLjMyIHNlY3M=".getBytes(StandardCharsets.UTF_8);
		//String encoded = Base64.getEncoder().encodeToString(message);
		byte[] decoded = Base64.getDecoder().decode(message);

		//System.out.println(encoded);
		System.out.println(new String(decoded, StandardCharsets.UTF_8));
	}

}
