package esocial.vallasmobile.ws.request;

import android.text.TextUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.ws.WsRequest;


public class GetUbicacionesRequest extends WsRequest {

	public GetUbicacionesRequest(VallasApplication context) {
		super(context);
	}

	public <T> T execute(String criteria, Integer from, Integer num, Class<T> responseClass) {

		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("estado", "1"));
		if(!TextUtils.isEmpty(criteria))
			params.add(new BasicNameValuePair("ubicacion", "%25[" + criteria.replace(" ", "%20") + "]%25"));
		params.add(new BasicNameValuePair("offset", from.toString()));
		params.add(new BasicNameValuePair("limit", num.toString()));

		return super.executeGetDefaultHeaders(Constants.UBICACIONES, params, responseClass);
	}

}