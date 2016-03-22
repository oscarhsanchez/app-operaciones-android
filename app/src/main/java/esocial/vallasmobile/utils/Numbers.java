package esocial.vallasmobile.utils;

public class Numbers {

	public static boolean checkIfNumber(String in) {

		if (Numbers.checkIfInteger(in)) return true;
		if (Numbers.checkIfLong(in)) return true;
		if (Numbers.checkIfFloat(in)) return true;
		if (Numbers.checkIfDouble(in)) return true;
		return false;

    }

	public static boolean checkIfLong(String in) {

		if (in == null) return false;
		if (in.equals("")) return false;

		try {

            Long.parseLong(in);

        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }

	public static boolean checkIfInteger(String in) {

		if (in == null) return false;
		if (in.equals("")) return false;

		try {

            Integer.parseInt(in);

        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }

	public static boolean checkIfFloat(String in) {

		if (in == null) return false;
		if (in.equals("")) return false;

		try {

			Float.parseFloat(in);

        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }

	public static boolean checkIfDouble(String in) {

		if (in == null) return false;
		if (in.equals("")) return false;

		try {

			Double.parseDouble(in);

        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }
	
	public static String formatTwoDecimals(double d){
		return String.format("%.2f",roundTwoDecimals(d));
	}

	public static double roundToDecimals(double d, int c) {
		//int temp=(int)((d*Math.pow(10,c)));
		//return (((double)temp)/Math.pow(10,c));
		//Redondeamos con valor absoluto ya que los .5 los redondea siempre hacia arriba y no es lo mismo en los numero negativos que positivos.
		if (d < 0)
			return (-1) * Math.round(Math.abs(d)*Math.pow(10,c))/Math.pow(10,c);
		else
			return Math.round(d*Math.pow(10,c))/Math.pow(10,c);
	}

	public static double roundTwoDecimals(double d) {
    	return roundToDecimals(d, 2);
		//DecimalFormat twoDForm = new DecimalFormat("#.##");
		//return Double.valueOf(twoDForm.format(d).replace(",", "."));

	}

	public static String intToString(int num, int digits){
		String output = Integer.toString(num);
		while (output.length() < digits) output = "0" + output;
		return output;
	}

	public static Boolean isEmpty(Integer numero){
		if (numero == null || numero.equals(null)) return true;
		return false;
	}

	public static Boolean isEmpty(Float numero){
		if (numero == null || numero.equals(null)) return true;
		return false;
	}

	public static Boolean isEmpty(Double numero){
		if (numero == null || numero.equals(null)) return true;
		return false;
	}
	
	public static boolean esEntero(Double numero){
	    if (numero % 1 == 0) {
	       return true;
	    }
	    else{
	    	return false;	
	    }
	}
}
