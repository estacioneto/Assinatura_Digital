package creating;

import java.security.PublicKey;

public class SignatureValidator {
	
	public static boolean validator(String message, PublicKey key, String originalMessage){
		return SignatureCreator.decrypt(message, key).equals(SignatureCreator.decrypt(originalMessage, key));
	}	
}