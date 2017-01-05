package start; 
import java.lang.*; 
import java.util.*; 

public class Divide { 

	public static void main(String[] args) throws DivideByZeroException { 

		if(args.length != 2) {
			System.out.println("Uzycie: java start.Divie 5 0"); 
			return; 
		}

		double result = div(Double.parseDouble(args[0]), Double.parseDouble(args[1])); 	

		System.out.println("Wynik dzielenia to: " + result); 
	}

	public static double div(double a, double b) throws DivideByZeroException {

		double result = 0; 

                if(b == 0) {
			throw new DivideByZeroException(String.valueOf(a), String.valueOf(b)); 
                } else {
                    result = a/b; 
                } 

		return result; 
	}

	static class DivideByZeroException extends Exception { 

		private String msg; 

		DivideByZeroException(String a, String b) { 
			msg = "Wykryto dzielenie przez zero w dzialaniu: " + a + "/" + b + "."; 
		}


		@Override 
		public String getMessage() { 
			return msg; 
		}

	};
	
}
