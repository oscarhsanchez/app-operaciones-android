package esocial.vallasmobile.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Dates {

	public final static String bdFormat = "yyyy-MM-dd";
	public final static String listFormat = "dd/MM/yyyy";
	public final static String diaMesFormat = "dd/MM";

	public static Date ConvertTimeStringToDate(String hora){
		//Espera    "HH:mm:ss"
		//Devuelve  Date
		if (TextUtils.isEmpty(hora)) return null;
		String[] arrTime = hora.split(":");
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arrTime[0]));
		c.set(Calendar.MINUTE, Integer.parseInt(arrTime[0]));
		c.set(Calendar.SECOND, Integer.parseInt(arrTime[0]));

		return c.getTime();
	}

	public static String ConvertSfDataStringToJavaString(String fecha){
		//Espera    "2011-12-13"
		//Devuelve  "13/12/2011"
		if (TextUtils.isEmpty(fecha)) return "";
		String[] arrdateTime = fecha.split("-");
		if (arrdateTime.length < 3) return "";
		return arrdateTime[2]+"/"+arrdateTime[1]+"/"+arrdateTime[0];
	}

	public static Date ConvertSfDataStringToDate(String fecha){
		//Espera    "2011-12-13"
		//Devuelve  Date
		if (TextUtils.isEmpty(fecha)) return null;
		String[] arrDate = fecha.split("-");
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(
				Integer.parseInt(arrDate[0]),
				Integer.parseInt(arrDate[1])-1,
				Integer.parseInt(arrDate[2]),
				0,
				0,
				0
				);

		return c.getTime();
	}

	public static String ConvertJavaStringToSfDataString(String fecha){
		//Espera    "13/12/2011"
		//Devuelve  "2011-12-13"
		if (TextUtils.isEmpty(fecha)) return "";
		String[] arrdateTime = fecha.split("/");
		if (arrdateTime.length < 3) return "";
		return arrdateTime[2]+"-"+arrdateTime[1]+"-"+arrdateTime[0];
	}

	public static String ConvertNetStringToJavaString(String fecha){
		//Espera    "2011-12-13T20:26:57.794"
		//Devuelve  "2011-12-13 20:26:57.794"
		if (TextUtils.isEmpty(fecha)) return "";
		String[] arrdateTime = fecha.split("T");
		return arrdateTime[0]+" "+arrdateTime[1];
	}

	public static Date ConvertStringToDate(String fecha, String separator){

		//"2011-12-13 20:26:57.794"
		if (fecha == null || fecha.equals("")) return new Date(0);

		String[] arrdateTime;

		if (TextUtils.isEmpty(separator)){
			arrdateTime = new String[]{ fecha, "00:00:00" };
		} else {
			arrdateTime = fecha.split(separator);
		}

		if (arrdateTime.length < 2) return new Date(0);

		String date = arrdateTime[0];
		String[] arrDate = date.split("-");
		String[] arrTime = {"00", "00", "00"};
		String millis = "000";

		if (arrdateTime.length > 1){
			String timeNano = arrdateTime[1];
			arrTime = timeNano.substring(0,8).split(":");
			millis = (timeNano.length() > 8) ? timeNano.substring(9) : "000";
		}

		Date fechaMills;
		try {
			Calendar c = Calendar.getInstance();
			c.clear();
			c.set(
					Integer.parseInt(arrDate[0]),
					Integer.parseInt(arrDate[1])-1,
					Integer.parseInt(arrDate[2]),
					Integer.parseInt(arrTime[0]),
					Integer.parseInt(arrTime[1]),
					Integer.parseInt(arrTime[2])
					);

			fechaMills = c.getTime();
			fechaMills = Dates.setMilliseconds(fechaMills, Integer.parseInt(millis));
			return new Date(fechaMills.getTime());

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Date(0);

	}

	public static String ConvertStringToDateFormat(String fecha, String separator, String format){
		SimpleDateFormat formateo = new SimpleDateFormat(format);
		Date date = Dates.ConvertStringToDate(fecha, separator);
		return formateo.format(date);
	}

	public static String ConvertStringToDateFormat(String fecha, String separator){
		SimpleDateFormat formateo = new SimpleDateFormat(Dates.listFormat);
		Date date = Dates.ConvertStringToDate(fecha, separator);
		return formateo.format(date);
	}

	public static String ConvertStringToTimeFormat(String fecha, String separator){
		SimpleDateFormat formateo = new SimpleDateFormat("HH:mm");
		Date date = Dates.ConvertStringToDate(fecha, separator);
		return formateo.format(date);
	}

	public static String format(Date fecha, String format){
		SimpleDateFormat formateo = new SimpleDateFormat(format);
		return formateo.format(fecha);
	}

	public static String format(Date fecha){
		SimpleDateFormat formateo = new SimpleDateFormat(Dates.listFormat);
		return formateo.format(fecha);
	}

	public static String formatToDBString(Date fecha){
		SimpleDateFormat formateo = new SimpleDateFormat(Dates.bdFormat);
		return formateo.format(fecha);
	}

	public static long ConvertStringToSQLTimestamp(String fecha, String separator){

		//long t = Timestamp.parse(fecha);
		//String a = "";
		//return t;

		//"2011-12-13T20:26:57.794"
		String[] arrdateTime = fecha.split(separator);
		String date = arrdateTime[0];
		String[] arrDate = date.split("-");
		String[] arrTime = {"00", "00", "00"};
		String millis = "000";

		if (arrdateTime.length > 1){
			String timeNano = arrdateTime[1];
			arrTime = timeNano.substring(0,8).split(":");
			millis = (timeNano.length() > 8) ? timeNano.substring(9) : "000";
		}

		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(
				Integer.parseInt(arrDate[0]),
				Integer.parseInt(arrDate[1])-1,
				Integer.parseInt(arrDate[2]),
				Integer.parseInt(arrTime[0]),
				Integer.parseInt(arrTime[1]),
				Integer.parseInt(arrTime[2])
				);

		Date fechaMills = c.getTime();
		fechaMills = Dates.setMilliseconds(fechaMills, Integer.parseInt(millis));

		return fechaMills.getTime();

	}

	// Helper function
	// Taken from org.apache.commons.lang.time
	private static Date set(Date date, int calendarField, int amount) {
		if (date == null) return null;
		// getInstance() returns a new object, so this method is thread safe.
		Calendar c = Calendar.getInstance();
		c.setLenient(false);
		c.setTime(date);
		c.set(calendarField, amount);
		return c.getTime();
	}

	public static Date setMilliseconds(Date date, Integer ms) {
		return set(date, Calendar.MILLISECOND, ms);
	}

	public static Calendar getCalendarFromDate(Date fecha){
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTime(fecha);
		return c;
	}

	public static Date getDateFromFields(Integer year, Integer month, Integer day){
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(
				year,
				month - 1,
				day
		);
		return c.getTime();
	}


	public static String getStringWSFromDate(Date pDate){
		Date date = (pDate != null) ? pDate : new Date(0);
		SimpleDateFormat formateo = new SimpleDateFormat("yyyy-MM-dd");
		String dateDef = formateo.format(date);

		Integer indexOfPlus = dateDef.indexOf("+");
		if (indexOfPlus > -1){
			dateDef = dateDef.substring(0, indexOfPlus);
		}

		return dateDef;
		//2012-02-06T05:05:33.292Z
	}

	public static String getStringWSFromStringDate(String pDate){
		String wsDate = (pDate == null || pDate.equals("")) ?  Dates.getStringWSFromDate(new Date(0)) : pDate;
		return wsDate;
	}

	public static String getStringWSFromDate(Date pDate, String format){
		Date date = (pDate != null) ? pDate : new Date(0);
		SimpleDateFormat formateo = new SimpleDateFormat(format);
		return formateo.format(date);
	}

	public static Date ConvertTextFieldStringToDate(String cad){
		String[] arrdateTime = cad.split("/");

		if (arrdateTime.length > 1){
			Calendar c = Calendar.getInstance();
			c.clear();
			c.set(
					Integer.parseInt(arrdateTime[2]),
					Integer.parseInt(arrdateTime[1])-1,
					Integer.parseInt(arrdateTime[0])
					);
			return c.getTime();
		}else{
			return new Date(0);
		}
	}

	public static String ConvertTimeWSToTimeJava(String strHora){
		//Espera    "HH:mm:ss"
		//Devuelve  "HH:mm"
		if (TextUtils.isEmpty(strHora)) return "";
        SimpleDateFormat from = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat to = new SimpleDateFormat("HH:mm");
        Date hora;
        try {
            hora = from.parse(strHora);
            return to.format(hora);
        } catch (ParseException e) {
            return null;
        }
	}

	public static String getMonthNameByNumber(int number){
		switch (number) {
		case 1:	return "Enero";	
		case 2:	return "Febrero";
		case 3:	return "Marzo";
		case 4:	return "Abril";
		case 5:	return "Mayo";
		case 6:	return "Junio";
		case 7:	return "Julio";	
		case 8:	return "Agosto";
		case 9:	return "Septiembre";
		case 10:return "Octubre";
		case 11:return "Noviembre";
		case 12:return "Diciembre";
		default :return null;
		}
	}

	public static Integer getNumberByMonthName(String month){
		Integer mes = 0;
		
		if(month.equalsIgnoreCase("Enero")) mes = 1;
		else if(month.equalsIgnoreCase("Febrero")) mes = 2;
		else if(month.equalsIgnoreCase("Marzo")) mes = 3;
		else if(month.equalsIgnoreCase("Abril")) mes = 4;
		else if(month.equalsIgnoreCase("Mayo")) mes = 5;
		else if(month.equalsIgnoreCase("Junio")) mes = 6;
		else if(month.equalsIgnoreCase("Julio")) mes = 7;
		else if(month.equalsIgnoreCase("Agosto")) mes = 8;
		else if(month.equalsIgnoreCase("Septiembre")) mes = 9;
		else if(month.equalsIgnoreCase("Octubre")) mes = 10;
		else if(month.equalsIgnoreCase("Noviembre")) mes = 11;
		else if(month.equalsIgnoreCase("Diciembre")) mes = 12;

		return mes;
	}
}
