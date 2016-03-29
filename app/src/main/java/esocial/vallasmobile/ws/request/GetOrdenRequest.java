package esocial.vallasmobile.ws.request;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.ws.WsRequest;


public class GetOrdenRequest extends WsRequest {

	public GetOrdenRequest(VallasApplication context) {
		super(context);
	}

	public <T> T execute(String pk_orden_trabajo, Class<T> responseClass) {

		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("extended", "1"));
		params.add(new BasicNameValuePair("codigo_user", context.getSession().codigo));
		params.add(new BasicNameValuePair("pk_orden_trabajo", pk_orden_trabajo));

		return super.executeGetDefaultHeaders(Constants.ORDENES, params, responseClass);
	}

}