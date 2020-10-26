package hr.fer.zemris.java.hw06.shell.commands.massrename;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Util;

/**
 * Razred koji modelira naredbu 'massrename' u shell okruženju
 * 
 * @author Luka Dragutin
 *
 */
public class MassRenameShellCommand implements ShellCommand {

	/** Naziv naredbe */
	private final static String COMMAND_NAME = "massrename";

	/** Opis naredbe */
	private final List<String> commandDescription = Collections.unmodifiableList(Arrays.asList(new String[] {
			"It can perform one of 4 operations.", "   -filter > filters the files in the given directory using the",
			"regular expression pattern",
			"   -groups > writes out the groups that satisfy regular expression groupations",
			"among the filtered files", "   -show > shows the layout and names of the files that are renamed using",
			"regular expression groups and/or just simple characters",
			"   -execute > performs the rename operation like in the 'show' operation",
			" i.e : massrename [DIR1] [DIR2] operation [reg expression] [rename pattern]" }));

	
	
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException Ako je zadan krivi broj ili oblik argumenata
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		var args = Util.getArguments(arguments);
		if (args.size() == 0) {
			throw new IllegalArgumentException("The command massrename can't run without arguments!");
		}
		if (args.size() < 4) {
			throw new IllegalArgumentException("Too few arguments!");
		}
		Path dir1 = env.getCurrentDirectory().resolve(Paths.get(args.get(0)));
		Path dir2 = env.getCurrentDirectory().resolve(Paths.get(args.get(1)));
		String pattern = args.get(3);
		switch (args.get(2)) {
		case "filter":
			if (args.size() != 4) {
				throw new IllegalArgumentException("Too much arguments!");
			}

			List<FilterResult> filtered;
			try {
				filtered = filter(dir1, pattern);
			} catch (IOException e) {
				throw new IllegalArgumentException("Illegal path... : " + e.getMessage());
			}

			filtered.forEach((e) -> env.writeln(e.toString()));
			break;

		case "groups":
			if (args.size() != 4) {
				throw new IllegalArgumentException("Too much arguments!");
			}

			groups(env, dir1, pattern);
			break;
		case "show":
			if (args.size() != 5) {
				throw new IllegalArgumentException("Too much arguments!");
			}
			show(env, dir1, dir2, pattern, args.get(4));
			break;
		case "execute":
			if (args.size() != 5) {
				throw new IllegalArgumentException("Too much arguments!");
			}
			execute(env, dir1, dir2, pattern, args.get(4));
			break;
		default:
			throw new IllegalArgumentException("Command '" + args.get(2) + "' is not valid!");
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Pomocna metoda za izvedbu operacije execute (premjestanje datoteka uz
	 * preimenovanje)
	 * 
	 * @param env     Okruzenje izvodenja programa
	 * @param dir1    Izvorni direktorij
	 * @param dir2    Odredisni direktorij
	 * @param pattern Uzorak po kojem se filtiraju datoteke za premjestanje
	 * @param mask    Uzorak za preimenovanje datoteka
	 */
	private void execute(Environment env, Path dir1, Path dir2, String pattern, String mask) {
		var fileNames = show(env, dir1, dir2, pattern, mask);
		for (var e : fileNames.entrySet()) {
			var source = dir1.resolve(e.getKey().toString());
			var target = dir2.resolve(e.getValue());
			try {
				Files.move(source, target);
			} catch (IOException e1) {
				throw new IllegalArgumentException(e1.getMessage());
			}
		}

	}

	/**
	 * Pomocna metoda za izvedbu operacije show (prikaz preimenovanja odabranih
	 * datoteka)
	 * 
	 * @param env     Okruzenje izvodenja programa
	 * @param dir1    Izvorni direktorij
	 * @param dir2    Odredisni direktorij
	 * @param pattern Uzorak po kojem se filtiraju datoteke za preimenovanje
	 * @param mask    Uzorak za preimenovanje datoteka
	 * @return
	 */
	private Map<FilterResult, String> show(Environment env, Path dir1, Path dir2, String pattern, String mask) {
		List<FilterResult> filtered;
		HashMap<FilterResult, String> newFileNames = new HashMap<>();
		try {
			filtered = filter(dir1, pattern);
		} catch (IOException e) {
			throw new IllegalArgumentException("Illegal path... : " + e.getMessage());
		}
		NameBuilderParser parser = new NameBuilderParser(mask);
		NameBuilder builder = parser.getNameBuilder();
		for (FilterResult f : filtered) {
			StringBuilder sb = new StringBuilder();
			builder.execute(f, sb);
			String newName = sb.toString();
			newFileNames.put(f, newName);
			env.writeln(dir1.toString() + "\\" + f.toString() + " => " + dir2.toString() + "\\" + newName);
		}
		return newFileNames;
	}

	/**
	 * Pomocna metoda za izvedbu operacije groups (ispis grupacija regularnih
	 * izraza)
	 * 
	 * @param env     Okruzenje izvrsavanja programa
	 * @param dir1    Izvorni direktorij
	 * @param pattern Uzorak za filtiranje i grupaciju
	 */
	private void groups(Environment env, Path dir1, String pattern) {
		List<FilterResult> filtered;
		try {
			filtered = filter(dir1, pattern);
		} catch (IOException e) {
			throw new IllegalArgumentException("Illegal path... : " + e.getMessage());
		}
		for (var f : filtered) {
			env.write(f.toString() + " ");
			for (int i = 0; i < f.numberOfGroups(); i++) {
				env.write(i + ": " + f.group(i) + " ");
			}
			env.writeln("");
		}
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDesription() {
		return commandDescription;
	}

	private static List<FilterResult> filter(Path dir, String pattern) throws IOException {
		Pattern p = Pattern.compile(pattern);
		ArrayList<FilterResult> list = new ArrayList<>();
		Files.list(dir).filter((e) -> p.asPredicate().test(e.getFileName().toString())).forEach((e) -> {
			var file = e.getFileName().toString();
			Matcher m = p.matcher(file);
			m.find();
			String[] group = new String[m.groupCount() + 1];
			for (int i = 0; i <= m.groupCount(); i++) {
				group[i] = m.group(i);
			}
			list.add(new FilterResult(e, group));
		});
		return list;
	}

}
