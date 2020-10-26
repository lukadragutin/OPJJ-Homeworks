package hr.fer.zemris.java.webserver.workers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radnik koji crta krug velicine 200 * 200 i salje ga klijentu
 * @author Luka Dragutin
 *
 */
public class CircleWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D g2d = bim.createGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, 200, 200);
		g2d.setColor(Color.ORANGE);
		g2d.fillOval(0, 0, 200, 200);
		g2d.dispose();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		context.setMimeType("image/png");
		try {
			ImageIO.write(bim, "png", bos);
			context.write(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
