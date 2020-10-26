package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Util;
import hr.fer.zemris.java.hw06.shell.lexer.ShellLexer;
import hr.fer.zemris.java.hw06.shell.lexer.ShellTokenType;

/**
 * Razred koji modelira naredbu 'hexdump' u okruženju shell.
 * 
 * @author Luka Dragutin
 *
 */
public class HexdumpShellCommand implements ShellCommand {

	/**
	 * Pomoćni razred koji modelira jedan redak hexdump operacije
	 * 
	 * @author Luka
	 *
	 */
	private static class hexDump {

		/**
		 * Broj pročitanih byteova
		 */
		private static int bytesRead = 0;
		/**
		 * Byteovi datoteke koja se obraduje
		 */
		private byte[] source;

		/**
		 * Heksadekadski zapis ivornih byteova
		 */
		private String[] hex;

		/**
		 * Tekstualna reprezentacija procitanih byteova
		 */
		private String text;

		/**
		 * Broj dotad procitanih byteova
		 */
		private String byteState;

		/**
		 * Duljina polja predanog na obradu
		 */
		private int len;

		public hexDump(byte[] source, int len) {
			this.source = source;
			byteState = Integer.toHexString(bytesRead);
			bytesRead += len;
			this.len = len;
			hex = new String[len];
			generateHexDump();
		}

		/**
		 * Generira hexdump zapis
		 */
		private void generateHexDump() {
			int i = 0;
			byte[] buffer = new byte[1];
			StringBuilder sb = new StringBuilder();
			for (var b : source) {
				if (i == len) {
					break;
				}
				if (b > 31 && b < 127) {
					sb.append((char) b);
				} else {
					sb.append('.');
				}
				buffer[0] = b;
				hex[i] = Util.bytetohex(buffer).toUpperCase();
				i++;
			}
			text = sb.toString();
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("0".repeat((8 - byteState.length()) < 0 ? 0 : 8 - byteState.length())//Ako je previše bajtova pročitano da stane u ispis
					+ (byteState.length() > 8 ? byteState.substring(byteState.length() - 8, byteState.length())       //Ispisuju se samo zadnjih 8 heksadekadskih brojeva zapisa
							: byteState)
					+ ": ");
			int i = 1;
			for (; i < 16; i++) {
				String x;
				if (i <= len) {
					x = hex[i - 1];
					sb.append(x);
				} else {
					sb.append("  ");
				}
				if (i == 8) {
					sb.append("|");
				} else {
					sb.append(" ");
				}
			}
			sb.append("| " + text);
			return sb.toString();
		}
	}

	private static final String COMMAND_NAME = "hexdump";
	private final List<String> commandDescription = Collections.unmodifiableList(
			Arrays.asList(new String[] { "The hexdump command reads a file from path given as an argument.",
					"It writes out the number of bytes in the file(hexadecimal), hexadecimal value of each byte",
					"and the text representation of bytes." }));

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IllegalArgumentException Ako su argumenti neispravni
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null) {
			throw new IllegalArgumentException("Must specify the file to hexdump!");
		}
		var lexer = new ShellLexer(arguments);
		var token = lexer.nextToken();
		if (token.getType().equals(ShellTokenType.EOF) || !lexer.nextToken().getType().equals(ShellTokenType.EOF)) {
			throw new IllegalArgumentException("Wrong number of arguments!");
		}
		Path file = env.getCurrentDirectory().resolve(token.getValue()).normalize();
		if (!file.toFile().isFile()) {
			throw new IllegalArgumentException("The argument has to be a path to a file!");
		}
		try (BufferedInputStream bs = new BufferedInputStream(Files.newInputStream(file))) {
			byte[] buffer = new byte[16];
			int status;
			hexDump.bytesRead = 0;
			while ((status = bs.read(buffer)) != -1) {
				var hexDump = new hexDump(buffer, status);
				env.writeln(hexDump.toString());
				buffer = new byte[16];
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		return null;
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDesription() {
		return commandDescription;
	}

}
