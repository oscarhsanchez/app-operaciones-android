package esocial.vallasmobile.ws.request;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.ws.WsRequest;


public class GetOrdenesMotivosRequest extends WsRequest {

	public GetOrdenesMotivosRequest(VallasApplication context) {
		super(context);
	}

	public <T> T execute(Class<T> responseClass) {

		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("fk_pais", context.getSession().fk_pais));

		return super.executeGetDefaultHeaders(Constants.ORDENES_MOTIVOS, params, responseClass);
	}

}