package hr.fer.zemris.java.blog.model.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Util {

	public static byte[] calcHash(String password) {
		MessageDigest sha;

		try {
			sha = MessageDigest.getInstance("SHA-256");
			byte[] bytes = password.getBytes();
			return sha.digest(bytes);
		} catch (NoSuchAlgorithmException ignorable) {
			return null;
		}
	}

	public static String hexEncode(byte[] bytes) {
		StringBuilder sb = new StringBuilder();  
		for (byte b : bytes) {
	        sb.append(String.format("%02X ", b));
	    }
		return sb.toString();
	}
	
	public static void sendError(HttpServletRequest req, HttpServletResponse resp, String message)
			throws ServletException, IOException {
		req.setAttribute("message", message);
		req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
	}
}
