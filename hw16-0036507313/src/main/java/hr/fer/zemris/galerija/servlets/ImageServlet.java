package hr.fer.zemris.galerija.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * {@link HttpServlet} that is used to render images
 * whose name is given as parameter and then using its
 * predecided directory, it is loaded and returned to
 * the user.
 * @author LukaD
 *
 */
@WebServlet(urlPatterns = {"/image"})
public class ImageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		
		if(name == null) {
			return;
		}
		
		String path = req.getServletContext().getRealPath("/WEB-INF/slike/" + name);
		
		Path picPath = Paths.get(path);
		if(!Files.exists(picPath)) {
			return;
		}
		
		ImageIO.write(ImageIO.read(picPath.toFile()), "jpg", resp.getOutputStream());
	}
}
