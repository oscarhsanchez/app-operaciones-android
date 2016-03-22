package esocial.vallasmobile.ws;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.utils.Utils;
import esocial.vallasmobile.ws.request.RenewTokenRequest;
import esocial.vallasmobile.ws.response.RenewTokenResponse;


public class WsRequest {

	public static final int REQUEST_TYPE_POST = 1;
	public static final int REQUEST_TYPE_GET = 2;
	public static final int REQUEST_TYPE_PUT = 3;
	public static final int REQUEST_TYPE_PATCH = 4;
	public static final int REQUEST_TYPE_DELETE = 5;

	private static Boolean goToRenew = false;

	protected VallasApplication context;

	public WsRequest(VallasApplication context) {
		this.context = context;
	}

	public WsResponse execute(String accept, String contentType, Integer requestType, String jsonAction, String parameters, Boolean addSessionToken, Boolean addSessionCookie, List<NameValuePair> headers) throws Exception {

		if (!Utils.checkInternetConnection(context)){
			Log.i("WsRequest", jsonAction + " - Sin conexion");
			return null;
		}

		Log.i("WsRequest", jsonAction);

		HttpClient httpClient = new DefaultHttpClient();
		StringEntity entity = null;
		if (parameters != null) entity = new StringEntity(parameters);
		HttpResponse httpResp = null;

		switch(requestType){
		case REQUEST_TYPE_POST:
			HttpPost post = new HttpPost(Constants.MAIN + jsonAction);
			post.addHeader("Content-type", contentType);
			if (!TextUtils.isEmpty(accept)) post.setHeader("Accept", accept);
			if (headers != null) {
				for (NameValuePair pair : headers) {
					post.addHeader(pair.getName(), pair.getValue());
				}
			}
			post.setEntity(entity);
			httpResp = httpClient.execute(post);
			break;
		case REQUEST_TYPE_PUT:
			HttpPut put = new HttpPut(Constants.MAIN + jsonAction);
			put.addHeader("Content-type", contentType);
			if (!TextUtils.isEmpty(accept)) put.setHeader("Accept", accept);
			if (headers != null) {
				for (NameValuePair pair : headers) {
					put.addHeader(pair.getName(), pair.getValue());
				}
			}
			put.setEntity(entity);
			httpResp = httpClient.execute(put);
			break;
		}

		String strResponse = EntityUtils.toString(httpResp.getEntity());

		Log.v("WsRequest response", strResponse);
		Log.v("WsRequest", "--------------------------------------------------------------------------------");
		Log.v("WsRequest", "--------------------------------------------------------------------------------");

		int statusCode = httpResp.getStatusLine().getStatusCode();

		WsResponse wsResponse = new WsResponse(statusCode, strResponse);

		Log.i("WsRequest", jsonAction + " - Terminado");

		return wsResponse;

	}

	/**
	 * Constructs a WsResponse object and calls the webservice to know the string response that will be
	 * deserialized later.
	 * @param accept
	 * @param contentType
	 * @param requestType
	 * @param jsonAction
	 * @param parameters
	 * @param addSessionToken
	 * @param addSessionCookie
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public WsResponse execute(String accept, String contentType, Integer requestType, String jsonAction, List<NameValuePair> parameters, Boolean addSessionToken, Boolean addSessionCookie, List<NameValuePair> headers) throws Exception {

		if (!Utils.checkInternetConnection(context)){
			Log.i("WsRequest", jsonAction + " - Sin conexion");
			return null;
		}

		Log.i("WsRequest", jsonAction);

		HttpClient httpClient = new DefaultHttpClient();
		UrlEncodedFormEntity entity = null;
		if (parameters != null) entity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
		HttpResponse httpResp = null;

		switch(requestType){
		case REQUEST_TYPE_POST:
			HttpPost post = new HttpPost(Constants.MAIN + jsonAction);
			post.addHeader("Content-type", contentType);
			if (!TextUtils.isEmpty(accept)) post.setHeader("Accept", accept);
			if (headers != null) {
				for (NameValuePair pair : headers) {
					post.addHeader(pair.getName(), pair.getValue());
				}
			}
			post.setEntity(entity);
			httpResp = httpClient.execute(post);
			break;
		case REQUEST_TYPE_GET:

			String param = "";
			for (int i=0; i<parameters.size(); i++){
				NameValuePair pair = parameters.get(i);
				param += TextUtils.isEmpty(param) ? "" : "&";
				param += pair.getName() + "=" + pair.getValue();
			}

			if (!TextUtils.isEmpty(param)) param = "?" + param;

			HttpGet get = new HttpGet(Constants.MAIN + jsonAction + param);
			get.addHeader("Content-type", contentType);
			get.addHeader("Accept", accept);
			if (headers != null) {
				for (NameValuePair pair : headers) {
					get.addHeader(pair.getName(), pair.getValue());
				}
			}
			httpResp = httpClient.execute(get);
			break;
		case REQUEST_TYPE_PUT:
			HttpPut put = new HttpPut(Constants.MAIN + jsonAction);
			put.addHeader("Content-type", contentType);
			put.addHeader("Accept", accept);
			if (headers != null) {
				for (NameValuePair pair : headers) {
					put.addHeader(pair.getName(), pair.getValue());
				}
			}
			put.setEntity(entity);
			httpResp = httpClient.execute(put);
			break;
		case REQUEST_TYPE_PATCH:
			HttpPatch patch = new HttpPatch(Constants.MAIN + jsonAction);
			patch.addHeader("Content-type", contentType);
			patch.addHeader("Accept", accept);
			if (headers != null) {
				for (NameValuePair pair : headers) {
					patch.addHeader(pair.getName(), pair.getValue());
				}
			}
			patch.setEntity(entity);
			httpResp = httpClient.execute(patch);
			break;
		case REQUEST_TYPE_DELETE:
			HttpDelete delete = new HttpDelete(Constants.MAIN + jsonAction);
			delete.addHeader("Content-type", contentType);
			delete.addHeader("Accept", accept);
			if (headers != null) {
				for (NameValuePair pair : headers) {
					delete.addHeader(pair.getName(), pair.getValue());
				}
			}
			delete.setEntity(entity);
			httpResp = httpClient.execute(delete);
			break;
		}

		String strResponse = EntityUtils.toString(httpResp.getEntity());

		Log.v("WsRequest response", strResponse);
		Log.v("WsRequest", "--------------------------------------------------------------------------------");
		Log.v("WsRequest", "--------------------------------------------------------------------------------");

		int statusCode = httpResp.getStatusLine().getStatusCode();

		WsResponse wsResponse = new WsResponse(statusCode, strResponse);

		Log.i("WsRequest", jsonAction + " - Terminado");

		return wsResponse;

	}

	/**
	 * Executes a webservice action, base method for all the others
	 * @param accion
	 * @param params
	 * @param clase
	 * @param method
	 * @param headers
	 * @param gson
	 * @return
	 */
	public <T> T execute(String accion, List<NameValuePair> params, Class<T> clase, int method, List<NameValuePair> headers, Gson gson) {
		T response = null;
		try {
			WsResponse wsResponse = execute("application/json", "application/x-www-form-urlencoded", 
					method, accion, params, true, false, headers);
			if (wsResponse != null) {
				Log.d(clase.getName(), wsResponse.getResult().toString());
				response = gson.fromJson(wsResponse.getResult(), clase);

				if (response instanceof WsResponse) {
					((WsResponse) response).setStatusCode(wsResponse.getStatusCode());
				}

				//Si invalid Token, hacemos refresh y volvemos a ejecutar la peticion
				if (response != null && ((WsResponse)response).error!=null && ((WsResponse)response).error.code==3000) {
                    RenewTokenRequest request = new RenewTokenRequest(context);
                    RenewTokenResponse refreshResponse = request.execute(RenewTokenResponse.class);
                    if(refreshResponse!=null){
                        if(!refreshResponse.failed()){
							//Guardamos la session
                            context.setSession(refreshResponse.Session);

                            wsResponse = execute("application/json", "application/x-www-form-urlencoded",
                                    method, accion, params, true, false, getDefaultHeaders());
                            if (wsResponse != null) {
                                Log.d(clase.getName(), wsResponse.getResult().toString());
                                response = gson.fromJson(wsResponse.getResult(), clase);
                            }

                            if (response instanceof WsResponse) {
                                ((WsResponse) response).setStatusCode(wsResponse.getStatusCode());
                            }
                            return response;
                        }
                    }else{
                        return response;
                    }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}


	/**
	 * Executes a WebService action with default deserializer and custom headers
	 * @param accion
	 * @param params
	 * @param clase
	 * @param method
	 * @param headers
	 * @return
	 */
	public <T> T execute(String accion, List<NameValuePair> params, Class<T> clase, int method, List<NameValuePair> headers) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return execute(accion, params, clase, method, headers, gson);
	}


	public <T> T executeGet(String accion, List<NameValuePair> params, Class<T> clase) {
		return execute(accion, params, clase, REQUEST_TYPE_GET, null);
	}

	public <T> T executePost(String accion, List<NameValuePair> params, Class<T> clase) {
		return execute(accion, params, clase, REQUEST_TYPE_POST, null);
	}

	public <T> T executePut(String accion, List<NameValuePair> params, Class<T> clase) {
		return execute(accion, params, clase, REQUEST_TYPE_PUT, null);
	}

	public <T> T executeGet(String accion, List<NameValuePair> params, List<NameValuePair> headerParams, Class<T> clase) {
		return execute(accion, params, clase, REQUEST_TYPE_GET, headerParams);
	}

	public <T> T executePost(String accion, List<NameValuePair> params, List<NameValuePair> headerParams, Class<T> clase) {
		return execute(accion, params, clase, REQUEST_TYPE_POST, headerParams);
	}

	public <T> T executePut(String accion, List<NameValuePair> params, List<NameValuePair> headerParams, Class<T> clase) {
		return execute(accion, params, clase, REQUEST_TYPE_PUT, headerParams);
	}

	public <T> T executeDelete(String accion, List<NameValuePair> params, List<NameValuePair> headerParams, Class<T> clase) {
		return execute(accion, params, clase, REQUEST_TYPE_DELETE, null);
	}

	public <T> T executePatch(String accion, List<NameValuePair> params, List<NameValuePair> headerParams, Class<T> clase) {
		return execute(accion, params, clase, REQUEST_TYPE_PATCH, null);
	}

	public <T> T executeGetDefaultHeaders(String accion, List<NameValuePair> params, Class<T> clase) {
		return execute(accion, params, clase, REQUEST_TYPE_GET, getDefaultHeaders());
	}

	public <T> T executePostDefaultHeaders(String accion, List<NameValuePair> params, Class<T> clase) {
		return execute(accion, params, clase, REQUEST_TYPE_POST, getDefaultHeaders());
	}

	public <T> T executePutDefaultHeaders(String accion, List<NameValuePair> params, Class<T> clase) {
		return execute(accion, params, clase, REQUEST_TYPE_PUT, getDefaultHeaders());
	}

	public <T> T executeDeleteDefaultHeaders(String accion, List<NameValuePair> params, Class<T> clase) {
		return execute(accion, params, clase, REQUEST_TYPE_DELETE, getDefaultHeaders());
	}

	public <T> T executePatchDefaultHeaders(String accion, List<NameValuePair> params, Class<T> clase) {
		return execute(accion, params, clase, REQUEST_TYPE_PATCH, getDefaultHeaders());
	}

	private List<NameValuePair> getDefaultHeaders() {
		List<NameValuePair> header = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair("Authorization", context.getSession().access_token));
		return header;

	}

	/**
	 * Executes a GET with default headers, and custom deserializer (used for Timeline deserializing)
	 * @param accion
	 * @param params
	 * @param clase
	 * @param deserializer
	 * @return
	 */
	public <T> T executeGetDefaultHeadersCustomDeserializer(String accion, List<NameValuePair> params, Class<T> clase, Gson deserializer) {
		return execute(accion, params, clase, REQUEST_TYPE_GET, getDefaultHeaders(), deserializer);
	}

}