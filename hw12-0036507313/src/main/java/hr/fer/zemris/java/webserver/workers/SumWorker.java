package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radnik koji prima dva parametra koje zbraja, ako nedostaju ili nisu valjani,
 * mijenja ih pretpostavljenom vrijednšću (prvog postavlja na 1, a drugog na 2)
 * Ako je rezultat paran ispisuje sliku {@value #evenImg}, a ako je rezultat neparan sliku {@value #oddImg}
 * @author Luka Dragutin
 *
 */
public class SumWorker implements IWebWorker {

	/**Ime slike koja se ispise ako je rjesenje  zbrajanja parno*/
	private final static String evenImg = "image1.jpg";
	
	/**Ime slike koja se ispise ako je rjesenje zbrajanja neparno*/
	private final static String oddImg = "image2.jpg";
	
	/**Put do skripte za generiranje html dokumenta za ispis rezultata zbrajanja i pripadne slike*/
	private final static String calcScript = "/private/pages/calc.smscr";
	
	@Override
	public void processRequest(RequestContext context) throws Exception {
		String a = context.getParameter("a");
		String b = context.getParameter("b");
		int aNum = 1;
		int bNum = 2;
		try {
			aNum = Integer.parseInt(a);
		} catch (NumberFormatException e1) {
		}
		try {
			bNum = Integer.parseInt(b);
		} catch (NumberFormatException e2) {
		}
		int result = aNum + bNum;
		context.setTemporaryParameter("varA", String.valueOf(aNum));
		context.setTemporaryParameter("varB", String.valueOf(bNum));
		context.setTemporaryParameter("zbroj", String.valueOf(result));
		context.setTemporaryParameter("imgName", result%2==0 ? evenImg : oddImg);
		context.getDispatcher().dispatchRequest(calcScript);
	}
}
