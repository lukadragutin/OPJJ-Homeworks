package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.webapp2.servlets.beans.BandInfo;

/**
 * Class that consists of methods used for loading band details and
 * voting details.
 * @author Luka Dragutin
 *
 */
public class LoadUtil {

	/**
	 * Loads the voting information on the survey
	 * @param req Current servlet context
	 * @return Vote results mapped on the id value
	 * @throws IOException If there happens a mistake while reading
	 */
	public static Map<String, Integer> loadVotes(HttpServletRequest req) throws IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Path filePath = Paths.get(fileName);

		if (!Files.exists(filePath)) {
			Files.createFile(filePath);
		}

		String file = Files.readString(filePath, StandardCharsets.UTF_8);
		Map<String, Integer> voteInfo = new HashMap<>();

		String[] bands = file.split("\n");
		for (String band : bands) {
			String[] votes = band.split("\t");
			if (votes.length != 2)
				continue;
			voteInfo.put(votes[0], Integer.parseInt(votes[1]));
		}

		return voteInfo;
	}

	/**
	 * Loads the band information that are part of the survey
	 * @param req Current servlet context
	 * @return Band information mapped on the id value
	 * @throws IOException If there occurs an error while reading
	 */
	public static TreeMap<String, BandInfo> loadBands(HttpServletRequest req) throws IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		String file = Files.readString(Paths.get(fileName), StandardCharsets.UTF_8);

		String[] bands = file.split("\n");
		TreeMap<String, BandInfo> bandMap = new TreeMap<>();
		for (String band : bands) {
			String[] info = band.split("\t");
			if (info.length != 3)
				continue;
			bandMap.put(info[0], new BandInfo(info[1], info[2]));
		}

		return bandMap;
	}
}
