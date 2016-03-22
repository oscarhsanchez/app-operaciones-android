package esocial.vallasmobile.ws.request;

import android.provider.Settings;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.ws.WsRequest;


public class RenewTokenRequest extends WsRequest {

	public RenewTokenRequest(VallasApplication context) {
		super(context);
	}

	public <T> T execute(Class<T> responseClass) {

        String cCode;
        if(Constants.isDebug)
            cCode = "MX";
        else
            cCode = Locale.getDefault().getCountry();

        String deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("renew_token", context.getSession().renew_token));
		params.add(new BasicNameValuePair("access_token", context.getSession().access_token));
		params.add(new BasicNameValuePair("countryid", cCode));
        params.add(new BasicNameValuePair("deviceid", deviceId));
		
		return super.executePost(Constants.LOGIN, params, responseClass);
	}

}