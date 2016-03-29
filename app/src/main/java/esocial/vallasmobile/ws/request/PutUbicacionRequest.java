package esocial.vallasmobile.ws.request;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.ws.WsRequest;


public class PutUbicacionRequest extends WsRequest {

    public PutUbicacionRequest(VallasApplication context) {
        super(context);
    }

    public <T> T executeLocation(String fk_ubicacion, Double latitude, Double longitude, Class<T> responseClass) {

        String accion = Constants.UBICACIONES + "?pk_ubicacion=" +fk_ubicacion;

        List<NameValuePair> param = new ArrayList<NameValuePair>(2);
        param.add(new BasicNameValuePair("latitud", latitude.toString()));
        param.add(new BasicNameValuePair("longitud", longitude.toString()));

        return super.executePutDefaultHeaders(accion, param, responseClass);
    }

    public <T> T executeBasicData(Class<T> responseClass) {

        List<NameValuePair> param = new ArrayList<NameValuePair>(2);
        //param.add(new BasicNameValuePair("entity", eSend));

        return super.executePutDefaultHeaders(Constants.UBICACIONES, param, responseClass);
    }

}