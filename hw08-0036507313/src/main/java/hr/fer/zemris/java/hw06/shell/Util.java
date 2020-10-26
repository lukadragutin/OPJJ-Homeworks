package hr.fer.zemris.java.hw06.shell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.lexer.ShellLexer;
import hr.fer.zemris.java.hw06.shell.lexer.ShellTokenType;

/**
 * Klasa koja nudi pomocne metode
 * @author Luka Dragutin
 */
public class Util {

	/**
	 * Metoda koja pretvara heksadekadski String zapis binarnog broja
	 * i pretvara ga u polje byte zapisa (jedan zapis jedan byte)
	 * @param keyText Heksadekadski String zapis za pretvorbu
	 * @return Polje byte pretvorenih vrijednosti
	 * @throws IllegalArgumentException Ako je heksadekadski zapis krivog formata
	 */
	public static byte[] hextobyte(String keyText) {
		char[] data = keyText.trim().toLowerCase().toCharArray();
		byte[] byteArray = new byte[keyText.length() / 2];
		if (data.length % 2 != 0) {
			throw new IllegalArgumentException("Wrong size of hex key!");
		}
		for (int i = 0, j = data.length; i + 1 < j; i += 2) {
			char y = data[i];
			char x = data[i + 1];
			var bytesx = getBytes(x).substring(4);
			var bytesy = getBytes(y).substring(4);
			if(bytesx.isEmpty() || bytesy.isEmpty()) {
				throw new IllegalArgumentException("Wrong character input!");
			}
			String bytes = bytesy + bytesx;
			byteArray[i / 2] = (byte) getFromBinary(bytes, true);

		}
		return byteArray;
	}

	/**
	 * Pretvara heksadekadsku znamenku u binarni zapis u formatu Stringa
	 * @param ch Heksadekadska znamenka
	 * @return String zapis binarnog broja
	 */
	private static String getBytes(char ch) {
		int digit = 0;
		if (Character.isDigit(ch)) {
			digit = Integer.parseInt(String.valueOf(ch));
		} else if (ch >= 'a' && ch <= 'f') {
			digit = getIntFromHex(ch);
		}
		else {
			throw new IllegalArgumentException("Wrong hex value!");
		}
		return getByteFromInt(digit);
	}

	/**
	 * Pomoćna metoda koja pretvara dekadski zapis u binarni String
	 * @param dekadski broj za pretvorbu
	 * @return Binarni zapis u Stringu nastao od dekadskog broja
	 */
	private static String getByteFromInt(int digit) {
		StringBuilder sb = new StringBuilder();
		boolean negative = false;
		if (digit < 0) {
			digit = 128 + digit;
			negative = true;

		}
		while (digit > 0) {
			sb.append(digit % 2);
			digit /= 2;
		}
		while (sb.length() < 7) {
			sb.append(0);
		}
		if (negative) {
			sb.append(1);
		} else
			sb.append(0);
		sb.reverse();
		return sb.toString();
	}

	/**
	 * Pomoćna metoda za pretvorbu heksadekadskih znamenki
	 * od 10 do 15 u dekadski zapis
	 * @param ch Heksadekadska znamenka od 10 do 15
	 * @return Integer zapis dekadskog broja ako je od
	 * a do f, -1 inače
	 */
	private static int getIntFromHex(char ch) {
		switch (ch) {
		case 'a':
			return 10;
		case 'b':
			return 11;
		case 'c':
			return 12;
		case 'd':
			return 13;
		case 'e':
			return 14;
		case 'f':
			return 15;
		default:
			return -1;
		}
	}

	/**
	 * Metoda koja pretvara polje bajtova u
	 * String zapis heksadekadskih vrijednosti.
	 * Svaki byte odgovara dvama znamenkama heksadekadskog zapisa
	 * @param bytearray Polje byteova za pretvorbu
	 * @return Heksadekadska vrijednost predanog polja byteova
	 */
	public static String bytetohex(byte[] bytearray) {
		if(Objects.requireNonNull(bytearray).length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (byte b : bytearray) {
			String byte1 = getByteFromInt(b);
			int x = getFromBinary(byte1.substring(0, 4), false);
			if (x < 10) {
				sb.append(x);
			} else {
				sb.append(hexFromInt(x));
			}
			x = getFromBinary(byte1.substring(4), false);
			if (x < 10) {
				sb.append(x);
			} else {
				sb.append(hexFromInt(x));
			}
		}
		return sb.toString();
	}

	/**
	 * Vraća String vrijednost heksadekadske znamenke
	 * za int vrijednost
	 * @param x Dekadska vrijednost broja
	 * @return String zapis heksadekadske znamenke od a do f
	 * ili prazan niz inače.
	 */
	private static String hexFromInt(int x) {
		switch (x) {
		case 10:
			return "a";
		case 11:
			return "b";
		case 12:
			return "c";
		case 13:
			return "d";
		case 14:
			return "e";
		case 15:
			return "f";
		default:
			return "";
		}

	}

	/**
	 * Pomoćna metoda koja pretvara binarni String zapis u dekadski broj,
	 * sa ili bez dvojnog komplementa
	 * @param b String binarni zapis
	 * @param complement Određivanje računanja u dvojnom komplementu
	 * @return Cijelobrojna vrijednost binarnog zapisa
	 */
	private static int getFromBinary(String b, boolean complement) {
		boolean negative = false;
		if (complement && b.charAt(0) == '1') {						
			var ch = b.toCharArray();								
			boolean added = false;
			for (int i = 0, j = b.length(); i < j; i++) {
				if (ch[j - i - 1] == '0') {
					ch[j - i - 1] = added ? '1' : '0';
				} else {
					ch[j - i - 1] = added ? '0' : '1';
					added = true;
				}
			}
			b = String.valueOf(ch);
			negative = true;
		}
		char lastBit;
		int i = 0;
		int hex = 0;
		while (i < b.length()) {
			lastBit = b.charAt(b.length() - i - 1);
			hex += Integer.parseInt(String.valueOf(lastBit)) * Math.pow(2, i);
			i++;

		}
		return negative ? -1 * hex : hex;
	}
	
	/**
	 * Dohvaca sve argumente iz niza, podrazumijeva 
	 * odvajanje razmakom
	 * @param arguments Niz argumenata odvojenih razmakom
	 * @return Listu argumenata parsiranih iz ulaznog neodvojenog niza
	 */
	public static List<String> getArguments(String arguments) {
		ArrayList<String> args = new ArrayList<>();
		if(arguments == null) {
			return args;
		}
		ShellLexer lexer = new ShellLexer(arguments);
		var token = lexer.nextToken();
		while(!token.getType().equals(ShellTokenType.EOF)) {
			args.add(token.getValue());
			token = lexer.nextToken();
		}
		return args;
	}
}
