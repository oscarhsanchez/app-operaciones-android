package esocial.vallasmobile.ws.request;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.ws.WsRequest;


public class PutOrdenMotivoRequest extends WsRequest {

    public PutOrdenMotivoRequest(VallasApplication context) {
        super(context);
    }

    public <T> T execute(String pk_orden_trabajo, String pk_motivo, Class<T> responseClass) {

        String accion = Constants.ORDENES + "?pk_orden_trabajo=" +pk_orden_trabajo;

        List<NameValuePair> param = new ArrayList<NameValuePair>(2);
        param.add(new BasicNameValuePair("fk_motivo", pk_motivo));
        param.add(new BasicNameValuePair("estado_orden", "4"));

        return super.executePutDefaultHeaders(accion, param, responseClass);
    }


}