package hr.fer.zemris.java.custom.scripting.exec;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred koji sadrzi pomocne operacije za upravljanje i operiranje
 * nad skriptama
 * @author Luka
 *
 */
public class Util {

	/**Mapa operatora spremljenih pod njihovim tekstualnim reprezentacijama 
	 * za brže i lakše dohvaćanje*/
	private static HashMap<String, Operator> operators;

	static {
		operators = new HashMap<>();
		operators.put("+", Operator.ADD);
		operators.put("-", Operator.SUB);
		operators.put("/", Operator.DIV);
		operators.put("*", Operator.MUL);
	}

	public static Operator getOperator(String name) {
		return operators.get(name);
	}

	/**
	 * Prima ime funkcije i stog koji sadrzi varijable te izvrsava zadane funkcije
	 * @param func Ime funkcije 
	 * @param stack Stog sa varijablama
	 * @param rc Kontekst komunikacije sa klijentom koji se ureduje
	 */
	public static void calculateFunction(String func, Stack<ValueWrapper> stack, RequestContext rc) {
		StackOperator so = new StackOperator();
		switch (func) {
		//Sinus
		case "sin":
			stack.push(new ValueWrapper(so.operate(stack.pop().getValue(), null, (x, y) -> Math.sin(x.intValue()))));
			return;
		//Decimalno formatiranje
		case "decfmt":
			var df = new DecimalFormat((String) stack.pop().getValue());
			String format = df.format(stack.pop().getValue());
			stack.push(new ValueWrapper(format));
			return;
		//Dupliciranje varijable sa vrha stoga
		case "dup":
			var dup = stack.peek();
			stack.push(dup);
			return;
		//Zamjena dviju varijabli sa vrha stoga
		case "swap":
			var x = stack.pop();
			var y = stack.pop();
			stack.push(x);
			stack.push(y);
			return;
		//Postavlja mimeType u razredu RequestContext
		case "setMimeType":
			rc.setMimeType(stack.pop().getValue().toString());
			return;
		case "paramGet":
			var dv = stack.pop().getValue();
			var name = stack.pop().getValue();
			var param = rc.getParameter((String) name);
			stack.push(new ValueWrapper(param == null ? dv : param));
			return;
		case "pparamGet":
			var dvp = stack.pop().getValue();
			var namep = stack.pop().getValue();
			var paramp = rc.getPersistentParameter((String) namep);
			stack.push(new ValueWrapper(paramp == null ? dvp : paramp));
			return;
		case "pparamSet":
			rc.setPersistentParameter(stack.pop().getValue().toString(), stack.pop().getValue().toString());
			return;
		case "pparamDel":
			rc.removePersistentParameter((String) stack.pop().getValue());
			return;
		case "tparamGet":
			var dvt = stack.pop().getValue();
			var namet = stack.pop().getValue();
			var paramt = rc.getTemporaryParameter((String) namet);
			stack.push(new ValueWrapper(paramt == null ? dvt : paramt));
			return;
		case "tparamSet":
			rc.setTemporaryParameter(stack.pop().getValue().toString(), stack.pop().getValue().toString());
			return;
		case "tparamDel":
			rc.removeTemporaryParameter(stack.pop().getValue().toString());
			return;
		case "now":
			String formatTime = (String) stack.pop().getValue();
			DateTimeFormatter dtf = null;
			if (formatTime != null) {
				try {
					dtf = DateTimeFormatter.ofPattern(formatTime);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			} else {
				dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");	
			}
			var time = LocalDateTime.now();
			stack.push(new ValueWrapper(time.format(dtf)));
		}
	}

}
