package hr.fer.zemris.java.hw06.crypto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Aplikacija koja omogućava korisniku provjeru valjanosti
 * datoteke i enkripciju i dekripciju datoteka uz valjani heksadekadski ključ.
 * Program prima traženu operaciju i put do datoteka preko komandne linije
 * @author Luka Dragutin
 *
 */
public class Crypto {


	public static void main(String[] args) {

		if (args.length < 2 || args.length > 3) {
			System.out.println("Invalid number of arguments!");
			System.exit(1);
		}
		Scanner sc = new Scanner(System.in);
		if (args[0].equals("checksha")) {
			System.out.println("Please provide expected sha-256 digest for "
					+ args[1].substring(args[1].lastIndexOf('\\') + 1) + ":");
			String digest = sc.next();
			String newDigest = getDigest((args[1]), "SHA-256");
			System.out.print("Digesting completed. ");
			if (digest.equals(newDigest)) {
				System.out.printf("Digest of %s matches expected digest.\n",
						args[1].substring(args[1].lastIndexOf('\\') + 1));
				sc.close();
				System.exit(0);
			} else {
				System.out.printf("Digest of %s does not match the expected digest. Digest was: %s",
						args[1].substring(args[1].lastIndexOf('\\') + 1), newDigest);
				sc.close();
				System.exit(0);
			}
		} else if (args[0].equals("encrypt") || args[0].equals("decrypt")) {
			boolean encrypt;
			System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits): ");
			String keyText = sc.next();
			System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
			String ivText = sc.next();
			if (args[0].equals("encrypt")) {
				encrypt = true;
			} else {
				encrypt = false;
			}
			try {
				crypt(args[1], args[2], keyText, ivText, encrypt);
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				sc.close();
				System.exit(1);
			}
			System.out.printf("Decryption completed. Generated file %s based on file %s.",
					args[2].substring(args[2].lastIndexOf('\\') + 1), args[1].substring(args[1].lastIndexOf('\\') + 1));
			sc.close();
		} else {
			System.out.println("Illegal command: '" + args[0] + "'.");
			sc.close();
			System.exit(1);
		}
	}

	/**
	 * Pomoćna metoda za enkripciju i dekripciju datoteka
	 * @param file Put do datoteke koja se obrađuje
	 * @param newFile Put za pohranu generirane datoteke
	 * @param keyText Heksadekadski ključ korišten pri enkripciji/dekripciji
	 * @param ivText Heksadekadska vrijednost za inicijalizaciju dekripcije/enkripcije
	 * @param encrypt Označava traži li se enkripcija ili dekripcija
	 * @throws IllegalArgumentException Ako su predani krivi algoritmi ili je došlo do greške
	 * prilikom enkripcije/dekripcije
	 */
	private static void crypt(String file, String newFile, String keyText, String ivText, boolean encrypt) {
		SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));
		Cipher cipher;
		try (var is = Files.newInputStream(Paths.get(file)); var os = Files.newOutputStream(Paths.get(newFile))) {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
			byte[] buffer = new byte[4096];
			while (true) {
				int status = is.read(buffer);
				if (status == -1)
					break;
				os.write(cipher.update(buffer, 0, status));
			}
			os.write(cipher.doFinal());
			os.close();
		} catch (IOException ex) {
			throw new IllegalArgumentException("Wrong file name!");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e2) {
			throw new IllegalArgumentException(e2.getMessage());
		} catch (InvalidKeyException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (InvalidAlgorithmParameterException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (IllegalBlockSizeException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (BadPaddingException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Generira i vraća heksadekadski zapis jedinstven svakoj datoteci
	 * koristeći predani algoritam generiranja
	 * @param file Put do datoteke koja se obrađuje
	 * @param algorithm Algoritam korišten pri generiranju zapisa
	 * @return Heksadekadski zapis jedinstvenog zapisa datoteke
	 * @throws IllegalArgumentException Ako je zadani kriv put do datoteke 
	 * ili netočan algoritam
	 */
	private static String getDigest(String file, String algorithm) {

		MessageDigest sha;
		try (var is = Files.newInputStream(Paths.get(file))) {
			sha = MessageDigest.getInstance(algorithm);
			while (true) {
				byte[] buffer = new byte[4096];
				int status = is.read(buffer);
				if (status == -1) {
					break;
				}
				sha.update(buffer, 0, status);
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("Wrong file name!");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("Wrong algorithm!");
		}
		return Util.bytetohex(sha.digest());
	}

}
