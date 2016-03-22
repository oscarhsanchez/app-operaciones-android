package esocial.vallasmobile.ws;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Vector;

public class SerializableObject {

    public SerializableObject(){

    }

    public static String[] getPrimaryKeyByClass(Class clase){
        String pk = null;

        HashMap<Class, String[]> mapPK = new HashMap<Class, String[]>();
        //mapPK.put(Client.class, new String[]{ "IMEI" });

        String[] pkAux = mapPK.containsKey(clase) ? mapPK.get(clase) : new String[]{  };

        return pkAux;
    }

    public static HashMap<Class, Class> getMapeosClases()
    {
        HashMap<Class, Class> mapeos = new HashMap<Class, Class>();
        /*mapeos.put(ArrayOfStars.class, Stars.class);
        mapeos.put(ArrayOfString.class, String.class);
        mapeos.put(ArrayOfCities.class, Cities.class);
        mapeos.put(ArrayOfClubs.class, Clubs.class);
        mapeos.put(ArrayOfImages.class, Images.class);
        mapeos.put(ArrayOfNews.class, News.class);
        mapeos.put(ArrayOfJoinedActivities.class, JoinedActivities.class);
        mapeos.put(ArrayOfErrors.class, Error.class);
        mapeos.put(ArrayOfSpotIn.class, SpotIn.class);
        mapeos.put(ArrayOfClubOffers.class, ClubOffers.class);
        mapeos.put(ArrayOfUser.class, User.class);
        mapeos.put(ArrayOfComment.class, Comment.class);*/
        return mapeos;
    }

    public Object get(String name) {
        Object valor = null;
        try {
            Class clase = getClass();
            valor = getClass().getField(name).get(this);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return valor;
    }

    public static Object RetrieveFromJsonArray(JSONArray jsonArray, Class claseJsonArray){
    	if (jsonArray == null) return null;

    	Object objeto = null;

    	try {
    		objeto = claseJsonArray.newInstance();

    		if (objeto instanceof Vector){

    			Class claseContentArray = getClassMappingByName(SerializableObject.getMapeosClases(), claseJsonArray);

		    	for (int i = 0; i < jsonArray.length(); ++i){
		    	    JSONObject jsonItem = jsonArray.getJSONObject(i);
		    	    Object elementoArray;
		    	    if (SerializableObject.isPrimitiveType(claseContentArray)){
                        Type tipo = (Type) claseContentArray;
                        elementoArray = SerializableObject.objectCastingPrimitive(tipo, jsonItem);
                        ((Vector) objeto).add(elementoArray);
                    }else{
                        elementoArray = SerializableObject.RetrieveFromJsonObject((JSONObject) jsonItem, claseContentArray);
                        ((Vector) objeto).add(claseContentArray.cast(elementoArray));
                    }
		    	}
    		}

    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}





		return objeto;
    }

    public static Object RetrieveFromJsonErrors(JSONArray jsonArray, Class claseJsonArray){
    	if (jsonArray == null) return null;

    	Object objeto = null;

    	try {
    		objeto = claseJsonArray.newInstance();

    		if (objeto instanceof Vector){

		    	for (int i = 0; i < jsonArray.length(); ++i){
		    	    String elementoArray = jsonArray.getString(i);
		    	    ((Vector) objeto).add(elementoArray);
		    	}
    		}

    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}





		return objeto;
    }

    public static <T> T RetrieveFromJsonObject(JSONObject jsonObj, Class<T> claseJsonObj) {

    	if (jsonObj == null) return null;

        T objeto = null;

        try {
            objeto = (T) claseJsonObj.newInstance();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (objeto instanceof Vector){


        	//objeto = SerializableObject.RetrieveFromJsonArray(jsonArray, claseJsonArray)

        } else {

            for (int i = 0; i < claseJsonObj.getFields().length; i++) {

                Field field = claseJsonObj.getFields()[i];

                //Log.v("Propiedad: ", field.getName());

                Type tipo = field.getType();
                Class clasePropiedad = (Class) tipo;
                Object valorPropiedad = null;

                if (jsonObj.has(field.getName())){

                    try {

                    	if (SerializableObject.isPrimitiveType(clasePropiedad)){

                            valorPropiedad = getValueFromJsonObject(tipo, field.getName(), jsonObj);

                        } else {

                        	if (Vector.class.isAssignableFrom(clasePropiedad)){

                        		valorPropiedad = (JSONArray) jsonObj.getJSONArray(field.getName());
                        		valorPropiedad = clasePropiedad.cast(SerializableObject.RetrieveFromJsonArray((JSONArray) valorPropiedad, clasePropiedad));

                        	}else{

	                        	valorPropiedad = (JSONObject) jsonObj.getJSONObject(field.getName());
	                            valorPropiedad = clasePropiedad.cast(SerializableObject.RetrieveFromJsonObject((JSONObject) valorPropiedad, clasePropiedad));
                        	}

                        }

                        field.set(objeto, valorPropiedad);
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }

            }
        }

    	/*




        if (objeto instanceof Vector){

            Class claseContentArray = getClassMappingByName(SerializableObject.getMapeosClases(), claseJsonObj);
            if (claseContentArray != null) {

            	JSONArray respJSON = new JSONArray();

                for (int iReg = 0; iReg < jsonObj.getPropertyCount(); iReg++){

                    Object jsonObjElemArray = (Object) jsonObj.getProperty(iReg);
                    Object elementoArray;

                    if (jsonObjElemArray instanceof SoapPrimitive) {
                        Type tipo = (Type) claseContentArray;
                        elementoArray = SerializableObject.objectCastingPrimitive(tipo, jsonObjElemArray);
                        ((Vector) objeto).add(elementoArray);
                    }else{
                        elementoArray = SerializableObject.RetrieveFromSoap((jsonObject) jsonObjElemArray, claseContentArray);
                        ((Vector) objeto).add(claseContentArray.cast(elementoArray));
                    }
                }

                //Log.i("Serializable object","insertando valores vector");

            }


        */
        return (T) objeto;
    }

    public static Class getClassMappingByName(
            HashMap<Class, Class> mapeosClasesArray, Class claseArray) {
        if (mapeosClasesArray.containsKey(claseArray))
            return mapeosClasesArray.get(claseArray);
        return null;
    }

    public static Object getValueFromJsonObject(Type tipo, String name, JSONObject obj){
    	Class clasePropiedad = (Class) tipo;
    	Object valorPropiedad = null;

    	try {
	    	if (tipo.equals(Integer.class)) {
	            valorPropiedad = obj.getInt(name);
	        } else if (tipo.equals(Double.class)) {
	            valorPropiedad = obj.getDouble(name);
	        } else if (tipo.equals(Long.class)) {
	            valorPropiedad = obj.getLong(name);
	        } else if (tipo.equals(Boolean.class)){
	            valorPropiedad = obj.getInt(name) == 1 ? true : false;
	        } else {
	            valorPropiedad = clasePropiedad.cast(obj.getString(name));
	        }
    	}catch(Exception e){
    		e.printStackTrace();
    	}

    	return valorPropiedad;

    }

    public static Boolean isPrimitiveType(Class<?> clazz){
		return clazz.equals(Boolean.class) ||
				clazz.equals(Integer.class) ||
				clazz.equals(Character.class) ||
				clazz.equals(Byte.class) ||
				clazz.equals(Short.class) ||
				clazz.equals(Double.class) ||
				clazz.equals(Long.class) ||
				clazz.equals(Float.class) ||
				clazz.equals(String.class) ||
				clazz.isEnum();
	}

    public static Object objectCastingPrimitive(Type tipo, Object valor){

        //if (valor == null) return null;

        Class clasePropiedad = (Class) tipo;
        Object valorPropiedad = null;

        if (tipo.equals(Integer.class)){
        	if (valor == null) return 0;
            valorPropiedad = Integer.parseInt(valor.toString());
        } else if (tipo.equals(Double.class)) {
        	if (valor == null) return Double.parseDouble("0");
            valorPropiedad = Double.parseDouble(valor.toString());
        } else if (tipo.equals(Float.class)) {
        	if (valor == null) return Float.parseFloat("0");
            valorPropiedad = Float.parseFloat(valor.toString());
        } else if (tipo.equals(Boolean.class)) {
        	if (valor == null) return false;
            valorPropiedad = (valor.toString().equals("true") || valor.toString().equals("1"));
        } else {
        	if (valor == null) return "";
            valorPropiedad = clasePropiedad.cast(valor.toString());
        }

        return valorPropiedad;
    }


}
