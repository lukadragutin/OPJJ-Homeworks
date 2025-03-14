package hr.fer.zemris.java.hw17.jvdraw.webapp;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.JVDraw;
import hr.fer.zemris.java.hw17.jvdraw.SaveVisitor;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;

public class JVDFile implements Comparable<JVDFile> {

	private String name;
	private List<GeometricalObject> objects;

	public JVDFile(String name, List<GeometricalObject> objects) {
		this.name = name;
		this.objects = objects;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the lines
	 */
	public List<GeometricalObject> getLines() {
		return objects;
	}

	/**
	 * @param lines the lines to set
	 */
	public void setObjects(List<GeometricalObject> lines) {
		this.objects = lines;
	}

	@Override
	public int hashCode() {
		return Objects.hash(objects, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof JVDFile))
			return false;
		JVDFile other = (JVDFile) obj;
		return Objects.equals(objects, other.objects) && Objects.equals(name, other.name);
	}

	@Override
	public int compareTo(JVDFile o) {
		return name.compareTo(o.getName());
	}

	public static void loadFiles(List<JVDFile> files, String pathString) throws IOException {
		FileVisitor<Path> fVisitor = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (Files.isRegularFile(file) && file.getFileName().toString().toLowerCase().endsWith(".jvd")) {
					files.add(JVDFile.loadJvdFile(file));
				}
				return FileVisitResult.CONTINUE;
			}

		};
		Path path = Paths.get(pathString);

		if (Files.isDirectory(path)) {
			Files.walkFileTree(path, fVisitor);
			files.sort(null);
		}
	}

	public static JVDFile loadJvdFile(Path file) throws IOException {
		List<String> lines = Files.readAllLines(file);
		List<GeometricalObject> objects;
		try {
			objects = JVDraw.readFile(lines);
		} catch (Exception ignorableException) {
			return null;
		}
		return new JVDFile(file.getFileName().toString(), objects);
	}
	
	public static boolean saveJvdFile(Path file, JVDFile jvd) throws IOException {
		Path path = file.resolve(jvd.getName() + ".jvd");
		Files.writeString(path, jvd.toString());
		return true;
	}
	
	@Override
	public String toString() {
		SaveVisitor sv = new SaveVisitor();
		objects.forEach(l -> l.accept(sv));

		return sv.toString();
	}
}
