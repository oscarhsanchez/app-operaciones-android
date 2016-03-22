package esocial.vallasmobile.ws.request;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.ws.WsRequest;


public class GetIncidenciasRequest extends WsRequest {

	public GetIncidenciasRequest(VallasApplication context) {
		super(context);
	}

	public <T> T execute(Class<T> responseClass) {

		List<NameValuePair> params = new ArrayList<NameValuePair>(2);

		return super.executeGetDefaultHeaders(Constants.INCIDENTS, params, responseClass);
	}

}