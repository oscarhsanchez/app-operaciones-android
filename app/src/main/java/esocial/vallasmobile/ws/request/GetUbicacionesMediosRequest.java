package esocial.vallasmobile.ws.request;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.ws.WsRequest;


public class GetUbicacionesMediosRequest extends WsRequest {

	public GetUbicacionesMediosRequest(VallasApplication context) {
		super(context);
	}

	public <T> T execute(String pk_ubicacion, Class<T> responseClass) {

		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("estado", "1"));
		params.add(new BasicNameValuePair("fk_ubicacion", pk_ubicacion));

		return super.executeGetDefaultHeaders(Constants.MEDIO, params, responseClass);
	}

}