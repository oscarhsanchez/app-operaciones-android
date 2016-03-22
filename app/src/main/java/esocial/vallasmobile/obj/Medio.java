package esocial.vallasmobile.obj;

import java.io.Serializable;

/**
 * Created by jesus.martinez on 22/03/2016.
 */
public class Medio implements Serializable{

    public String pk_medio;
    public String fk_pais;
    public String fk_ubicacion;
    public String fk_subtipo;
    public Integer posicion;
    public String id_cara;
    public String tipo_medio;
    public String estatus_iluminacion;
    public String visibilidad;
    public String estatus_inventario;
    public Integer slots;
    public Double coste;
    public Integer estado;
    public String token;
    public String created_at;
    public String updated_at;
}
