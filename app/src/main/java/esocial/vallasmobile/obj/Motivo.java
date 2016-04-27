package esocial.vallasmobile.obj;

import java.io.Serializable;

/**
 * Created by jesus.martinez on 27/04/2016.
 */
public class Motivo implements Serializable {

    public String pk_motivo;
    public String fk_pais;
    public String descripcion;
    public Integer tipo_incidencia;
    public String created_at;
    public String updated_at;
    public String token;
    public Integer estado;
}
