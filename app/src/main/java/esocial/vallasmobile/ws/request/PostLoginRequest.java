package esocial.vallasmobile.ws.request;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.ws.WsRequest;


public class PostLoginRequest extends WsRequest {

	public PostLoginRequest(VallasApplication context) {
		super(context);
	}

	public <T> T execute(String username, String password, String countryId, String deviceId, Class<T> responseClass) {
				
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("countryid", countryId));
        params.add(new BasicNameValuePair("deviceid", deviceId));
		
		return super.executePost(Constants.LOGIN, params, responseClass);
	}

}