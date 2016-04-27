package esocial.vallasmobile.obj;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.utils.Dates;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public class GeoLocalizacion implements Serializable {

    public String pk_user_geo;
    public String fk_user;
    public String fecha;
    public Double longitud;
    public Double latitud;

    public GeoLocalizacion (VallasApplication context, Double latitud, Double longitud){
        fk_user = context.getSession().fk_user;
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fecha = sdf.format(now);
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
