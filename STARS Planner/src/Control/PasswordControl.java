package Control;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class PasswordControl{
	
    public static String getHash(String password) {
        String generatedPassword = null;
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-1");
	        md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
