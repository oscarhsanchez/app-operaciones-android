package esocial.vallasmobile.obj;

import java.io.Serializable;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public class Orden implements Serializable {


    public String pk_orden_trabajo;
    public String fk_pais;
    public String fk_propuesta;
    public String fk_medio;
    public String fk_motivo;
    public String codigo_user;
    public Integer tipo;
    public Integer estado_orden;
    public String fecha_limite;
    public String fecha_cierre;
    public String observaciones;
    public String observaciones_cierre;
    public String version;
    public String campania;
    public Ubicacion ubicacion;
    public Integer estado;
    public String token;
    public String created_at;
    public String updated_at;

}
