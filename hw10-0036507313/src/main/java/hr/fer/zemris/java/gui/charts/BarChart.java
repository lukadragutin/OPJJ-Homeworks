package hr.fer.zemris.java.gui.charts;

import java.util.List;
import java.util.Objects;

/**
 * Razred koji implementira model jednostavnog tablicnog grafa
 * cije su vrijednosti spremljene kao primjerci razreda {@link XYValue}
 * @author Luka Dragutin
 *
 */
public class BarChart {

	/** Minimalna vrijednost y komponente */
	private int yMin;
	
	/** Maksimalna vrijednost y komponente */
	private int yMax;
	
	/** Korak(razmak) izmedu dvije susjedne y komponente*/
	private int yStep;
	
	/** Naziv x osi, naziv vrijednosti koje ona predstavlja */
	private String xTitle;

	/** Naziv y osi, naziv vrijednosti koje ona predstavlja */
	private String yTitle;
	
	/** Lista vrijednosti grafa */
	private List<XYValue> values;
	
	/**
	 * Stvara novi primjerak razreda {@link BarChart}.
	 * @param values Lista vrijednosti grafa
	 * @param xTitle Naziv x osi
	 * @param yTitle Naziv y osi
	 * @param yMin Minimalna vrijednost na y osi
 	 * @param yMax Maksimalna vrijednost na y osi
	 * @param yStep Razmak izmedu dvaju susjednih y vrijednosti
	 * @throws RuntimeException 
	 * <i> Ako je {@link #yMin} negativan
	 * <i> Ako je {@link #yMin} veci od {@link #yMax}
	 * <i> Ako je neka od vrijednosti iz {@link #values} manja od {@link #getyMin()}
	 */
	public BarChart(List<XYValue> values, String xTitle, String yTitle, int yMin, int yMax, int yStep) {
		if(yMin < 0) {
			throw new RuntimeException("Negative values not allowed!");
		}
		
		if(yMax <= yMin) {
			throw new RuntimeException("Max value must be greater than the minimal value!"); 
		}
		values.forEach((e) -> {if(e.getY() < yMin) {
			throw new RuntimeException("Incoherent values given!");
		}});
		this.yMin = yMin;
		this.yMax = yMax;
		this.yStep = yStep;
		this.xTitle = Objects.requireNonNull(xTitle);
		this.yTitle = Objects.requireNonNull(yTitle);
		this.values = Objects.requireNonNull(values);
		values.sort((e1, e2) -> e1.getX() - e2.getX());
	}
	
	/** Getter za {@link #values}l */
	public List<XYValue> getValues() {
		return values;
	}

	/** Getter za {@link #yMin}*/
	public int getyMin() {
		return yMin;
	}

	/** Getter za {@link #yMax}*/
	public int getyMax() {
		return yMax;
	}

	/**Getter za {@link #yStep}*/
	public int getyStep() {
		return yStep;
	}

	/**Getter za {@link #xTitle}*/
	public String getxTitle() {
		return xTitle;
	}

	/**Getter za {@link #yTitle}*/
	public String getyTitle() {
		return yTitle;
	}
}
