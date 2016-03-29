package esocial.vallasmobile.ws.request;

import android.text.TextUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.utils.Dates;
import esocial.vallasmobile.ws.WsRequest;


public class PutOrdenRequest extends WsRequest {

    public PutOrdenRequest(VallasApplication context) {
        super(context);
    }

    public <T> T execute(String pk_orden_trabajo, Integer estado, String observaciones, Class<T> responseClass) {

        String accion = Constants.ORDENES + "?pk_orden_trabajo=" +pk_orden_trabajo;

        List<NameValuePair> param = new ArrayList<NameValuePair>(2);
        if(estado.equals(2)) {
            param.add(new BasicNameValuePair("fecha_cierre", Dates.getStringWSFromDate(new Date(), "yyyy-MM-dd HH:mm:ss")));
            if (!TextUtils.isEmpty(observaciones)) {
                param.add(new BasicNameValuePair("observaciones_cierre", observaciones));
            }
        }
        param.add(new BasicNameValuePair("estado_orden", estado.toString()));

        return super.executePutDefaultHeaders(accion, param, responseClass);
    }


}