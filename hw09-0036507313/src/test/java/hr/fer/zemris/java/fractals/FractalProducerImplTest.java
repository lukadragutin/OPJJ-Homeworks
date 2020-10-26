package hr.fer.zemris.java.fractals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

class FractalProducerImplTest {

	@Test
	void calculateFractal() {
		Complex c = new Complex(0, -1);
		ComplexRootedPolynomial f = new ComplexRootedPolynomial(Complex.ONE, Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG);
		ComplexPolynomial p = f.toComplexPolynom();
		FractalProducerImpl fp = new FractalProducerImpl(f);

		int index = fp.findRoot(c, p, p.derive());
		assertEquals(3, index);
	}

}
