package esocial.vallasmobile.utils;

/**
 * Created by jesus.martinez on 17/12/2015.
 */
public class Constants {

    public static Boolean isDebug = true;
    public static int refreshLocationTime = 20; //20 min
    public static long minTime = 5 * 60 * 1000; // Minimum time interval for update in seconds, i.e. 5 minutes.
    public static long minDistance = 0; // Minimum distance change for update in meters
    public static long sendImageFrequency = 5 * 60 * 1000; //in ms 5min


    //---------------------------------RUTAS---------------------------------------//

    public static String MAIN = "http://api.gpovallas.com/";//"192.168.0.20/";
    public static String LOGIN = "login";
    public static String ORDENES = "ordenes";
    public static String ORDENES_IMAGES = "ordenes/imagenes";

    public static String INCIDENCIAS = "incidencias";
    public static String INCIDENCIAS_IMAGES = "incidencias/imagenes";

    public static String UBICACIONES = "ubicaciones";
    public static String UBICACIONES_IMAGES = "ubicaciones/imagenes";

    public static String GEOLOCALIZACION = "usuarios/geo";

    public static String MEDIO = "medios";



    //--------------------------------SHARED PREFERENCES---------------------------//

    public static String SESSION = "session";
    public static String REFRESH_ORDENES = "refreshOrdenes";
    public static String REFRESH_INCIDENCIAS = "refreshIncidencias";

    //--------------------------------PERMISSION--------------------------------------//

    public static final int PERMISSION_LOCATION = 100;
    public static final int PERMISSION_READ_STORAGE = 101;


    public static final int REQUEST_GALLERY = 1000;
    public static final int REQUEST_CAMERA = 1001;
    public static final int REQUEST_ORDEN = 1002;
    public static final int REQUEST_ADD_INCIDENCIA = 1003;
    public static final int REQUEST_SELECT_UBI = 1004;
    public static final int REQUEST_SELECT_MEDIO = 1005;
    public static final int REQUEST_CHANGE_ORDEN_STATUS = 1006;
    public static final int REQUEST_CHANGE_INCIDENCIA_STATUS = 1007;
}
