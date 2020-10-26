package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.java.thread.DaemonicThreadFactory;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Implementacija sucelja {@link IFractalProducer}
 * @see IFractalProducer
 * @author Luka Dragutin
 *
 */
public class FractalProducerImpl implements IFractalProducer {

	/** Bazen dretvi */
	private ExecutorService pool;
	
	/** Vrijednost kompleksnog polinoma sa nultockama
	 * kojem se racunaju fraktali  */
	private ComplexRootedPolynomial f;
	
	/** Kompleksni polinom bez nultocki*/
	ComplexPolynomial polynomial;
	
	/** Derivacija polinoma  */
	ComplexPolynomial derived;

	/** Maksimalni broj iteracija pri racunanju fraktala */
	private final static int MAX_ITER = 16*16*16;
	
	/** Granica odstupanja kovergencije */
	private final static double CONVERGENCE_THRESHOLD = 1E-3;
	
	/** Granica odstupanja nultocki  */
	private final static double ROOT_THRESHOLD = 2E-3;

	/** Broj podjela poslova */
	private final static int FRACTAL_DIVISION = 8 * Runtime.getRuntime().availableProcessors();

	public FractalProducerImpl(ComplexRootedPolynomial f) {
		pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new DaemonicThreadFactory());
		this.f = Objects.requireNonNull(f);
		polynomial = f.toComplexPolynom();
		derived = polynomial.derive();
	}

	/**
	 * Racuna vrijednosti fraktala za odredeni dio realne ravnine
	 * @param reMin Minimalna vrijednost realne komponente
	 * @param reMax Maksimalna vrijedost realne komponente
	 * @param imMin Minimalna vrijednost imaginarne komponente
	 * @param imMax Maksimalna vrijednost imaginarne komponente
	 * @param width Sirina ekrana
	 * @param height Visina ekrana
	 * @return Vrijednosti fraktala
	 */
	public short[] calculateFractal(double reMin, double reMax, double imMin, double imMax, int width, int height) {
		short[] data = new short[width * height];

		double realIncrement = (Math.abs(reMax - reMin)) / width;
		double imIncrement = (Math.abs(imMax - imMin)) / height;

		int offset = 0;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int index = findRoot(new Complex(reMin + j * realIncrement, imMin + i * imIncrement), polynomial,
						derived);
				data[offset++] = (short) (index + 1);
			}
		}
		return data;
	}

	
	 /**
	  * {@inheritDoc}
	  */ 
	@Override
	public void produce(double arg0, double arg1, double arg2, double arg3, int arg4, int arg5, long arg6,
			IFractalResultObserver arg7, AtomicBoolean arg8) {

		/**
		 * Posao za izvodenje u visedretvenom okruzenju 
		 * @author Luka Dragutin
		 *
		 */
		class Job implements Callable<short[]> {

			double imMax;
			double imMin;
			double reMax;
			double reMin;
			int width;
			int heigth;

			public Job(double reMin, double reMax, double imMin, double imMax, int width, int heigth) {

				this.imMax = imMax;
				this.imMin = imMin;

				this.reMax = reMax;
				this.reMin = reMin;

				this.width = width;
				this.heigth = heigth;
			}

			@Override
			public short[] call() {
				return calculateFractal(reMin, reMax, imMin, imMax, width, heigth);
			}

		}
		System.out.println("Pocinjem izracun...");
		ArrayList<Job> jobList = new ArrayList<>();

		double imIncrement = Math.abs((arg3 - arg2)) / (double) (FRACTAL_DIVISION);
		double height = arg5 / (double) FRACTAL_DIVISION;
		int height2 = arg5 / FRACTAL_DIVISION;
		double extra = 0;

		for (int y = 0; y < FRACTAL_DIVISION; y++) {

			double imMin = arg2 + y * imIncrement;
			double imMax = arg2 + (y + 1) * imIncrement;
			extra = extra + (height - height2);
			if (extra - (int) extra == 0) {
				jobList.add(new Job(arg0, arg1, imMin, imMax, arg4, height2 + (int) extra));
				extra = 0;
			} else {
				jobList.add(new Job(arg0, arg1, imMin, imMax, arg4, height2));
			}
		}

		List<Future<short[]>> results = new ArrayList<>();
		jobList.forEach(e -> results.add(pool.submit(e)));

		short[] result = new short[0];
		for (var r : results) {
			while (true) {
				try {
					var data = r.get();
					result = concatanate(result, data);
					break;
				} catch (ExecutionException | InterruptedException e) {
					continue;
				}
			}
		}
		arg7.acceptResult(result, (short) (f.toComplexPolynom().order() + 1), arg6);
	}

	/* Pomocna metoda za spajanje vrijednosti dva polja u jedno */
	private static short[] concatanate(short[] result, short[] data) {
		short[] newArray = new short[result.length + data.length];
		System.arraycopy(result, 0, newArray, 0, result.length);
		System.arraycopy(data, 0, newArray, result.length, data.length);
		return newArray;
	}

	/**
	 * Metoda koja preko talyorovog polinoma iteriranjem dolazi
	 * do vrijednosti koja odgovara nekoj od nultocki polinoma
	 * @param c Kompleksni broj za koji se racuna
	 * @param polynomial Polinom za kojeg se racuna fraktal
	 * @param derived Derivacija polinoma
	 * @return Vraca indeks nultocke koja je najbliza vrijednosti izracunatoj
	 * iteriranjem kroz taylorov red
	 */
	public int findRoot(Complex c, ComplexPolynomial polynomial, ComplexPolynomial derived) {
		int iter = 0;
		double module;
		Complex zn = c.add(Complex.ZERO);

		do {
			Complex numerator = polynomial.apply(zn);
			Complex denominator = derived.apply(zn);
			Complex znold = zn;
			Complex fraction;
			try {
				fraction = numerator.divide(denominator);
			} catch (ArithmeticException e) {
				break;
			}
			zn = zn.sub(fraction);

			module = znold.sub(zn).module();
			iter++;
		} while (iter <= MAX_ITER && module > CONVERGENCE_THRESHOLD);

		return f.indexOfClosestRootFor(zn, ROOT_THRESHOLD);
	}

}
