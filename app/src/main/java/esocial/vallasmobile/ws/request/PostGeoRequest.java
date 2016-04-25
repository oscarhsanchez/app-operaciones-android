package esocial.vallasmobile.ws.request;

import android.util.Base64;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.obj.GeoLocalizacion;
import esocial.vallasmobile.obj.Incidencia;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.ws.WsRequest;


public class PostGeoRequest extends WsRequest {

    public PostGeoRequest(VallasApplication context) {
        super(context);
    }

    public <T> T execute(ArrayList<GeoLocalizacion> localizaciones, Class<T> responseClass) {

        Gson gson = new Gson();
        String jSend = gson.toJson(localizaciones);
        String eSend = "";
        try {
            eSend = new String(Base64.encodeToString(jSend.getBytes("UTF-8"), Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        List<NameValuePair> param = new ArrayList<NameValuePair>(2);
        param.add(new BasicNameValuePair("array", eSend));

        return super.executePostDefaultHeaders(Constants.GEOLOCALIZACION, param, responseClass);
    }

}