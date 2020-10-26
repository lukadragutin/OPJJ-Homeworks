package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Pomocni razred za operacije nad razredom {@link ObjectMultistack}
 * @author Luka
 *
 */
public class StackOperator {

	/**
	 * Omogucava vrsenje matematickih operacija nad predanim argumentima
	 * Operacija koju ce izvrsiti je zadana sa {@link Operator}
	 * @param x Prvi argument
	 * @param y Drugi argument
	 * @param o Matematicki operator
	 * @return rezultat operacije
	 * @throws RuntimeException Ako vrijednosti nisu ispravnog brojevnog formata
	 */
	public Object operate(Object x, Object y, Operator o) {
		Object ofX;
		Object ofY;
		try {
			ofX = process(x);
		} catch (RuntimeException e1) {
			throw new RuntimeException("Current value type not valid!");
		}
		try {
			ofY = process(y);
		} catch (RuntimeException e2) {
			throw new RuntimeException("Argument type not valid!");
		}
		if (ofX instanceof Double || ofY instanceof Double) {
			double arg1, arg2;
			if(!(ofX instanceof Double)) {
				arg1 = (double) ((int) ofX);
			}
			else {
				arg1 = (double) ofX;
			}
			if(!(ofY instanceof Double)) {
				arg2 = (double) ((int) ofY);
			}
			else {
				arg2 = (double) ofY;
			}
			return ValueWrapper.getOperationsDouble().get(o).execute(arg1, arg2);
		} else {
			return ValueWrapper.getOperationsInt().get(o).execute((Integer) ofX, (Integer) ofY);

		}
		
	}

	
	/**
	 * Priprema objekt za matematicku operaciju provedenu {@link StackOperator#operate(Object, Object, Operator)}
	 * @return Obradeni objekt
	 * @throws RuntimeException Ako nije ispravnog formata
	 */
	public Object process(Object x) {
		if (x == null) {
			return Integer.valueOf(0);

		}
		if (x instanceof String) {
			if (((String) x).contains(".") || ((String) x).contains("E")) {
				try {
					return Double.valueOf(Double.parseDouble((String) x));
				} catch (NumberFormatException e) {
					throw new RuntimeException("Wrong format!");
				}
			} else {
				try {
					return Integer.valueOf(Integer.parseInt((String) x));
				} catch (NumberFormatException e) {
					throw new RuntimeException("Wrong format!");
				}
			}
		}
		if (x instanceof Double || x instanceof Integer) {
			return x;
		}
		throw new RuntimeException();
	}
}
