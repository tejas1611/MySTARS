package Control;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import Entity.Password;


public class PasswordControl {
	 
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
    
    public static Password generateHash(String password) {
        String generatedPassword = null;
        byte[] salt = new byte[16];
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            salt = getSalt();
	        md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new Password(generatedPassword, salt);
    }
    
    public static boolean comparePassword(String enteredPassword, Password studPassword) {
    	boolean equal = false;
    	String hashedPassword = studPassword.getPassword();
    	String generatedPassword = null;
    	byte[] salt = studPassword.getSalt();
    	try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
	        md.update(salt);
            byte[] bytes = md.digest(enteredPassword.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    	if(generatedPassword.equals(hashedPassword)) {
    		equal = true;
    	}
    	
    	return equal;
    }
}
