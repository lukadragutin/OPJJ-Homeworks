package hr.fer.zemris.galerija.servlets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
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
 * {@link HttpServlet} that processes images and
 * generates thumbnails of them if they are not already generated and
 * saved in its 'cache'. If they are already saved, the thumbnail is
 * just forwarded to the client with a significant time save.
 * @author LukaD
 *
 */
@WebServlet(urlPatterns = {"/thumbnail"})
public class ThumbnailServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		String thumbName = "t_" + name;
		String parentDir = req.getServletContext().getRealPath("/WEB-INF");
		String thumbDir = parentDir + "/thumbnails";
		if(!Files.exists(Paths.get(thumbDir))) {
			Files.createDirectory(Paths.get(thumbDir));
		}
		
		String thumbPathS = thumbDir + "/" + thumbName;
		Path thumbPath = Paths.get(thumbPathS);
		
		BufferedImage b;
		
		if(!Files.exists(thumbPath)) {
			String picPathS = parentDir + "/slike/" + name;
			Path picPath = Paths.get(picPathS);
			
			if(!Files.exists(picPath)) {
				return;
			}
			
			BufferedImage bim = ImageIO.read(Files.newInputStream(picPath));
			
			b = new BufferedImage(150, 150, BufferedImage.TYPE_3BYTE_BGR);
			Graphics g = b.getGraphics();
			
			g.drawImage(bim, 0, 0, 150, 150, Color.WHITE, null);
			
			ImageIO.write(b, "jpg", thumbPath.toFile());
			g.dispose();
		}
		
		else {
			b = ImageIO.read(Files.newInputStream(thumbPath));
		}
		
		ImageIO.write(b, "jpg", resp.getOutputStream());
	}
}
