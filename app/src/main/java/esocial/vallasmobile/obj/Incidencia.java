package esocial.vallasmobile.obj;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public class Incidencia implements Serializable {

    /*Tipos de incidencias:
        Fijacion: Tipo = 0
        Monitorizacion: Tipo = 1
        Instalacion: Tipo = 2
        Iluminacion: Tipo = 3
        Plano: Tipo = 4
        Otros: Tipo = 5*/

    public static String[] tipos = { "Fijación", "Monitorización", "Instalación", "Iluminación",
            "Plano", "Otros"};

    public String pk_incidencia;
    public String fk_pais;
    public String fk_medio;
    public String codigo_user;
    public String codigo_user_asignado;
    public Integer tipo;
    public Integer estado_incidencia;
    public String fecha_limite;
    public String fecha_cierre;
    public String observaciones;
    public String observaciones_cierre;
    public Integer estado;
    public String token;
    public String created_at;
    public String updated_at;

    public Ubicacion ubicacion;
}
