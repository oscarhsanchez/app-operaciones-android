package esocial.vallasmobile.ws.request;

import android.location.Location;
import android.text.TextUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.ws.WsRequest;


public class GetOrdenesRequest extends WsRequest {

	public GetOrdenesRequest(VallasApplication context) {
		super(context);
	}

	public <T> T execute(String criteria, Location location, String estado_orden, Class<T> responseClass) {

		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("extended", "1"));
        params.add(new BasicNameValuePair("estado", "1"));

		if(location!=null) {
			params.add(new BasicNameValuePair("latitud", String.valueOf(location.getLatitude())));
			params.add(new BasicNameValuePair("longitud", String.valueOf(location.getLongitude())));
		}

		params.add(new BasicNameValuePair("estado_orden", estado_orden));
		params.add(new BasicNameValuePair("codigo_user", context.getSession().codigo));
        params.add(new BasicNameValuePair("sort", "[fecha_limite_ASC]"));
		if(!TextUtils.isEmpty(criteria))
			params.add(new BasicNameValuePair("search", criteria.replace(" ", "%20")));

		return super.executeGetDefaultHeaders(Constants.ORDENES, params, responseClass);
	}

}