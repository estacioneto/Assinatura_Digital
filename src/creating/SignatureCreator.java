package creating;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
   

public class SignatureCreator {

    public static final String ALGORITHM = "RSA";
    private static final char[] HEX_ARRAY= "0123456789ABCDEF".toCharArray();
   
    /*
     * Local de acesso a chave privada da assinature
     */
    public static final String PATH_PRIVATE_KEY = "key/private.key";
   
    /*
     * Local de acesso a chave publica da assinatura
     */
    public static final String PATH_PUBLIC_KEY = "key/public.key";
   
    public SignatureCreator() {
    	
    	if (!this.isSomewhere()) {
	          this.generateKeys(PATH_PRIVATE_KEY, PATH_PUBLIC_KEY);
	          
	    }
   		
	}
    
    /*
     * Processo para gerar as chaves e guardar em arquivos
     */
    public void generateKeys(String Path1, String Path2) {
      try {
    	  
        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(1024);
        /*
         * Par de chaves
         */
        final KeyPair key = keyGen.generateKeyPair();
   
        File privateKeyFile = new File(Path1);
        File publicKeyFile = new File(Path2);
   
        /*
         * Se nao houver local existente, o local sera criado
         */
        if (privateKeyFile.getParentFile() != null) {
          privateKeyFile.getParentFile().mkdirs();
        }
        
        privateKeyFile.createNewFile();
   
        if (publicKeyFile.getParentFile() != null) {
          publicKeyFile.getParentFile().mkdirs();
        }
        
        publicKeyFile.createNewFile();
   
        /*
         * Salva chaves
         */
        ObjectOutputStream publicKeyWriter = new ObjectOutputStream(
            new FileOutputStream(publicKeyFile));
        publicKeyWriter.writeObject(key.getPublic());
        publicKeyWriter.close();
   
        ObjectOutputStream privateKeyWriter = new ObjectOutputStream(
            new FileOutputStream(privateKeyFile));
        privateKeyWriter.writeObject(key.getPrivate());
        privateKeyWriter.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
   
    }
   
    /*
     * Verifica se as chaves existem
     */
    public boolean isSomewhere() {
   
      File privateKey = new File(PATH_PRIVATE_KEY);
      File publicKey = new File(PATH_PUBLIC_KEY);
   
      if (privateKey.exists() && publicKey.exists()) {
        return true;
      }
      
      return false;
    }
   
    /*
     * Gera hash
     */
    public byte[] generateHash(String signature) {
    	MessageDigest m;
    	byte cipherText[] = null;
		try {
			m = MessageDigest.getInstance("MD5");
			cipherText = m.digest(signature.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return cipherText;
    }
    
    /*
     * Transforma um array de bytes em uma String hexadecimal
     */
    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    

    /*
     * Transforma uma String hexadecimal em um array de bytes
     */
    private static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    /*
     * Encripta hash usando chave privada
     */
    public String encrypt(byte[] signature, PrivateKey key) {
        byte[] cipherText = null;
        
        try {
          final Cipher cipher = Cipher.getInstance(ALGORITHM);

          /*
           * Como trabalhamos com Assinatura digital, a chave privada
           * eh usada para criptografar
           */
          
          cipher.init(Cipher.ENCRYPT_MODE, key);
          cipherText = cipher.doFinal(signature);
        } catch (Exception e) {
          e.printStackTrace();
        }
        
        return this.bytesToHex(cipherText);
      }
    
   
    /*
     * Decriptografa a mensagem usando a chave publica (static por ser acessado
     * tanto pela Main quanto pelo validador)
     */
    public static String decrypt(String message, PublicKey key) {
      byte[] decryptedText = null;
      
      try {
        final Cipher cipher = Cipher.getInstance(ALGORITHM);

        
        cipher.init(Cipher.DECRYPT_MODE, key);      
        decryptedText = cipher.doFinal(hexToBytes(message));
   
      } catch (Exception ex) {
        return "";
      }
   
      return new String(decryptedText);
    }
    
    /*
     * Imprime os bytes de um array
     */
    public String printBytes(byte[] b){
    	String bytes = "[";
    	
    	for(int i = 0; i < b.length; i++)
    		bytes += " " + b[i] + ",";
    	
    	bytes += "]";
    	return bytes;
    }
   
}
