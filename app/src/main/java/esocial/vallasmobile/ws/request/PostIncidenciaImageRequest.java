package esocial.vallasmobile.ws.request;

import android.graphics.Bitmap;
import android.util.Base64;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.obj.IncidenciaImagen;
import esocial.vallasmobile.obj.OrdenImagen;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.ws.WsRequest;


public class PostIncidenciaImageRequest extends WsRequest {

    public PostIncidenciaImageRequest(VallasApplication context) {
        super(context);
    }

    public <T> T execute(IncidenciaImagen imagen, Class<T> responseClass) {

        Gson gson = new Gson();
        String jSend = gson.toJson(imagen);
        String eSend = "";
        try {
            eSend = new String(Base64.encodeToString(jSend.getBytes("UTF-8"), Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        List<NameValuePair> param = new ArrayList<NameValuePair>(2);
        param.add(new BasicNameValuePair("entity", eSend));

        return super.executePostDefaultHeaders(Constants.INCIDENCIAS_IMAGES, param, responseClass);
    }

}