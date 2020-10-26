package hr.fer.zemris.java.hw17.jvdraw.webapp.servlets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.webapp.JVDFile;

/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/slika")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		if (name == null) {
			return;
		}
		
		String pathString = req.getServletContext().getRealPath("/WEB-INF/images");
		Path home = Paths.get(pathString);
		
		var file = home.resolve(name);
		
		JVDFile jvdFile = JVDFile.loadJvdFile(file);
		
		var objects = jvdFile.getLines();
		
		GeometricalObjectBBCalculator bbCalculator = new GeometricalObjectBBCalculator();
		objects.forEach(l -> l.accept(bbCalculator));
		
		BufferedImage bImage = createImage(bbCalculator.getBoundingBox(), objects);
		
		ImageIO.write(bImage, "png", resp.getOutputStream());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private static BufferedImage createImage(Rectangle bbox, List<GeometricalObject> objects) {
		BufferedImage image = new BufferedImage(bbox.width, bbox.height, BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, bbox.width, bbox.height);
		g2d.translate(-bbox.x, -bbox.y);

		GeometricalObjectPainter paint = new GeometricalObjectPainter(g2d);
		objects.forEach(l -> l.accept(paint));
		g2d.dispose();
		return image;
	}

}
