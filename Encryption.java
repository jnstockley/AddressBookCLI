import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
	byte[] input;
	byte[] keyBytes = "putKey".getBytes();
	byte[] ivBytes = "putKey".getBytes();
	SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
	IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
	Cipher cipher;
	byte[] cipherText;
	int ctLength;
	byte[] plainText;
	
	public String encrypt(String string) {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			input = string.getBytes();
			cipher = Cipher.getInstance("DES/CTR/NoPadding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
			cipherText = new byte[cipher.getOutputSize(input.length)];
			ctLength = cipher.update(input, 0, input.length, cipherText,0);
			ctLength += cipher.doFinal(cipherText, ctLength);
		}catch (Exception e){
			e.printStackTrace();
		}
		return new String (cipherText);
	}
	
	public String decrypt(byte[] cipherText) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
		try {
			ctLength = cipherText.length;
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			cipher = Cipher.getInstance("DES/CTR/NoPadding", "BC");
			cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
			plainText = new byte[cipher.getOutputSize(ctLength)];
			int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);//TODO Find error Bad Arguments
			ptLength+= cipher.doFinal(plainText, ptLength);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new String (plainText);
	}
}
