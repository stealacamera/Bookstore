package Bookstore.Bookstore.commons.utils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Identity {
	public static String hashPassword(String password) {
		KeySpec spec = new PBEKeySpec(password.toCharArray(), new byte[] {12, 120, 65}, 10000, 128);
		
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = factory.generateSecret(spec).getEncoded();
			
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
