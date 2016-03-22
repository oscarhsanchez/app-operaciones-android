package esocial.vallasmobile.obj;

import java.io.Serializable;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public class Ubicacion implements Serializable{

    public String  pk_ubicacion;
    public String fk_pais;
    public String fk_empresa;
    public String unidad_negocio;
    public String tipo_medio;
    public String fk_plaza;
    public String fk_zona_fijacion;
    public String fk_zona_instalacion;
    public String fk_zona_iluminacion;
    public String estatus; //
    public String ubicacion;//
    public String direccion_comercial;
    public String referencia;
    public String categoria; //
    public String catorcena;
    public Integer anio;
    public String fecha_instalacion;
    public String observaciones;
    public String trafico_vehicular; // coche
    public String trafico_transeuntes; // personita
    public String nivel_socioeconomico;
    public String lugares_cercanos;
    public Double latitud;
    public Double longitud;
    public Integer reserva;
    public Integer estado;
    public String token;
    public String created_at;
    public String updated_at;
}
