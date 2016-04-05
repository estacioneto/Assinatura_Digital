package creating;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

public class Main {
	

//		Assinatura digital consiste em um sistema que verifica a autenticidade de
//um documento, nao buscando criptografar o mesmo. A ideia eh basicamente permitir que
//o remetente assine o documento com uma chave privada e passe a chave publica para
//o destinatario, que por sua vez, ira conferir a integridade e autenticidade do documento.
//
//	O documento pode ser criptografado ou nao, pois o foco da assinatura digital eh apenas
//garantir que o documento eh valido.
//
//
// 		Um algoritmo bom que pode ser utilizado na criacao de uma assinatura criptografada
//por tratar chaves (publica e privada) eh o algoritmo RSA. Para a implementacao do sistema,
//podemos utilizar bibliotecas que nos axiliam nesse processo.
//
//
//
//	A base do nosso trabalho vai girar em torno dos conceitos e exemplos dados no site :
//http://www.devmedia.com.br/criptografia-assimetrica-criptografando-e-descriptografando-dados-em-java/31213

	/*
	 * Em NENHUM momento, a assinatura eh armazenada.
	 * Apenas armazena-se o conteudo CRIPTOGRAFADO!
	 */
	
	/*
	 * 	Como tratamos de assinatura digital, a mensagem a ser
	 * criptografada eh a propria assinatura, portanto o arquivo a ser enviado
	 * e assinado NAO tem necessidade de ter a informacao sigilosa, pois o 
	 * que a assinatura garante eh a integridade e a validacao do arquivo.
	 */
	private static Scanner scan = new Scanner(System.in);
	private static String encryptedSignature;
	private static PublicKey publicKey;
	private static PrivateKey privateKey;
	private static SignatureCreator creator;
	
	public static void main(String[] args) {
		   
	      try {
	   
	        menu();
	        
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	    }

	private static void menu() throws Exception{
   
		initializeKeys();
        readSignature();
        
	    while(true){
	    	
	    	System.out.println("O que deseja fazer?");
	    	System.out.println();
	    	System.out.println("1- Nova Assinatura");
	    	System.out.println("2- Verificar Assinatura");
	    	System.out.println("3- Imprimir assinatura");
	    	System.out.println("4- Sair");
	    	System.out.println();
	    	
	    	int option = scan.nextInt();
	    	scan.nextLine();
	    	
	    	switch(option){
	    	case 1:
	    		readSignature();
	    		break;
	    	case 2:
	    		verifySignature(encryptedSignature);
	    		break;
	    	case 3:
	    		System.out.println(encryptedSignature);
	    		break;
	    	default:
	    		break;
	    	}
	    	
	    	if(option == 4)
	    		break;
	    }
	    scan.close();
	}

	private static void initializeKeys() throws Exception{
		creator = new SignatureCreator();
		
		ObjectInputStream inputStreamPublic = null;
        ObjectInputStream inputStreamPrivate = null;     

        /*
         * Mensagem eh criptografada
         */
        inputStreamPublic = new ObjectInputStream(new FileInputStream(SignatureCreator.PATH_PUBLIC_KEY));
        publicKey = (PublicKey) inputStreamPublic.readObject();
        inputStreamPrivate = new ObjectInputStream(new FileInputStream(SignatureCreator.PATH_PRIVATE_KEY));
        privateKey = (PrivateKey) inputStreamPrivate.readObject();

        inputStreamPublic.close();
	    inputStreamPrivate.close();
	}

	
	private static void verifySignature(String originalSignature) {
		System.out.println("Digite a Assinatura:");
        String signature = scan.nextLine();
        
        /*
         * Assinatura como foi recebida.
         */
        if(SignatureValidator.validator(signature, publicKey, originalSignature))
        	System.out.println("Assinatura valida!");
        else
        	System.out.println("Cuidado! Assinatura invalida!");
	}

	
	private static void readSignature() {
		System.out.println("Digite a Assinatura:");
        
		/*
		 * Primeiro gera o hash com a chave privada
		 */
        encryptedSignature = creator.encrypt(creator.generateHash(scan.nextLine()), privateKey);
	}
	
	
}
