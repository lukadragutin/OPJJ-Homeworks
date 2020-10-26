package hr.fer.zemris.galerija.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * <p>Utility class that offers different operations with
 * the image gallery and provides information about it.</p>
 *<p>The methods implemented allow its user to search image gallery
 *by tags, get all the avaliable tags and also searching images by name.</p>
 *
 * @author LukaD
 *
 */
public class GalleryLoader {

	/**
	 * The path to the file containing all the informations about
	 * the image gallery
	 */
	private static Path opisnik;
	
	/**
	 * A map containing tags and their belonging pictures
	 */
	private static Map<String, List<String>> tagPics;

	static {
		opisnik = null;
		tagPics = null;
	}
	
	/**
	 * Searches the information file and returns all
	 * of the avaliable tags from the images.
	 * @return set of all the tags
	 */
	public static Set<String> getTags() {
		if (tagPics == null) {
			loadTagMap();
		}

		return tagPics.keySet();
	}


	/**
	 * Searches the gallery information looking
	 * for data on the image whose name is given as parameter
	 * {@code name} and returns it as an instance of the class {@link Picture}
	 * @param name The name of the searched image
	 * @return The data on the searched image packed in an instance
	 * of the class {@link Picture}
	 * @throws RuntimeException If the path to the gallery description file is
	 * not set or the searched image doesn't exist
	 */
	public static Picture getPictureInfo(String name) {
		if(opisnik == null) {
			throw new RuntimeException("Descriptor file not initialized.");
		}
		
		
		Picture p = new Picture();
		p.setName(name);

		List<String> lines;
		try {
			lines = Files.readAllLines(opisnik);
		} catch (IOException e) {
			throw new RuntimeException("Descriptor file not initialized.");
		}

		int i = 0;
		for (String s : lines) {
			if (s.trim().equals(name)) {
				p.setDesc(lines.get(i + 1));
				p.setTag(lines.get(i + 2));
				return p;
			}
			i++;
		}
		return null;
	}
	
	/**
	 * A setter for the {@link #opisnik}
	 */
	public static void setDescriptor(String path) {
		opisnik = Paths.get(path);
	}
	
	/**
	 * Checks whether the path to the descriptor file is set
	 * @return If it is set if returns <code>true</code> and
	 * if not, returns <code>null</code>
	 */
	public static boolean isDecriptorSet() {
		return opisnik != null;
	}
	

	/**
	 * Searches the description file to fing pictures that
	 * contain the given tag
	 * @param tag Search filter for the images
	 * @return A list of all the picture names that have the
	 * tag given as parameter
	 */
	public static List<String> getPicturesWithTag(String tag) {
		if (tagPics == null) {
			loadTagMap();
		}
		return tagPics.get(tag);
	}


	/**
	 * A helping method to load all the data about tags into the
	 * field {@link #tagPics} so not to search through the file everytime
	 */
	private static void loadTagMap() {
		List<String> lines;
		try {
			lines = Files.readAllLines(opisnik);
		} catch (IOException e) {
			throw new RuntimeException("Invalid descriptor file path.");
		}

		tagPics = new HashMap<>();
		List<String> pics;

		int i = 0;
		for (String s : lines) {
			if (i % 3 == 2) {
				String[] tagsArray = s.split(",");
				for (String tags : tagsArray) {
					pics = tagPics.get(tags.trim());
					if (pics == null) {
						pics = new ArrayList<>();
						pics.add(lines.get(i - 2).trim());
						tagPics.put(tags.trim(), pics);
					} else {
						pics.add(lines.get(i - 2).trim());
					}
				}
			}
			i++;
		}

	}
}
