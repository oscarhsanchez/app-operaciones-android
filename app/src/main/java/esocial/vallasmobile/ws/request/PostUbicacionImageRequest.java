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
import esocial.vallasmobile.obj.UbicacionImagen;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.ws.WsRequest;


public class PostUbicacionImageRequest extends WsRequest {

    public PostUbicacionImageRequest(VallasApplication context) {
        super(context);
    }

    public <T> T execute(String pk_ubicacion, String nombre, Bitmap bitmap, Class<T> responseClass) {

        UbicacionImagen imagen = new UbicacionImagen();
        imagen.fk_ubicacion = pk_ubicacion;
        imagen.fk_pais = context.getSession().fk_pais;
        imagen.nombre = nombre;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        imagen.data = Base64.encodeToString(byteArray, Base64.DEFAULT);

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

        return super.executePostDefaultHeaders(Constants.UBICACIONES_IMAGES, param, responseClass);
    }

}