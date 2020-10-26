package hr.fer.zemris.lsystems.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;

class LSystemBuilderImplTest {

	
	private static LSystem createKochCurve(LSystemBuilderProvider provider) {
		return provider.createLSystemBuilder()
					.registerCommand('F', "draw 1")
					.registerCommand('+', "rotate 60")
					.registerCommand('-', "rotate -60")
					.setOrigin(0.05, 0.4)
					.setAngle(0)
					.setUnitLength(0.9)
					.setUnitLengthDegreeScaler(1.0/3.0)
					.registerProduction('F', "F+F--F+F")
					.setAxiom("F")
					.build();
		}

	@Test
	void testGenerateZero() {
		var ls1 = createKochCurve(LSystemBuilderImpl::new);
		String levelZero = ls1.generate(0);
		assertEquals("F", levelZero);
	}
	
	@Test
	void testGenerateOne() {
		var ls1 = createKochCurve(LSystemBuilderImpl::new);
		String levelOne = ls1.generate(1);
		assertEquals("F+F--F+F", levelOne);
	}
	
	@Test
	void testGenerateTwo() {
		var ls1 = createKochCurve(LSystemBuilderImpl::new);
		String levelTwo = ls1.generate(2);
		assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", levelTwo);
	}
	

}
