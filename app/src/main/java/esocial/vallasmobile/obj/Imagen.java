package esocial.vallasmobile.obj;

import java.io.Serializable;

/**
 * Created by jesus.martinez on 22/03/2016.
 */
public class Imagen implements Serializable{

    public String pk_archivo;
    public String fk_pais;
    public String nombre;
    public String path;
    public String url;
    public String observaciones;
    public String observaciones_cliente;
    public Integer estado_imagen;
    public Integer estado;
    public String token;
    public String created_at;
    public String updated_at;

    public String data;
}
