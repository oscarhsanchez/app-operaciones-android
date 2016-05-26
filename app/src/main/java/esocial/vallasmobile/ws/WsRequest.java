package esocial.vallasmobile.ws;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
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


	/**
	 * Constructs a WsResponse object and calls the webservice to know the string response that will be
	 * deserialized later.
	 *
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

		if (!Utils.checkInternetConnection(context)) {
			Log.i("WsRequest", jsonAction + " - Sin conexion");
			return null;
		}

		Log.i("WsRequest", jsonAction);

		URL url;
		HttpURLConnection conn = null;
		String param = "";
		for (int i = 0; i < parameters.size(); i++) {
			NameValuePair pair = parameters.get(i);
			param += TextUtils.isEmpty(param) ? "" : "&";
			param += pair.getName() + "=" + pair.getValue();
		}
		byte[] postData = param.getBytes(Charset.forName("UTF-8"));
		int postDataLength = postData.length;

		switch (requestType) {
			case REQUEST_TYPE_POST:
				url = new URL(Constants.MAIN + jsonAction);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				break;

			case REQUEST_TYPE_GET:
				if (!TextUtils.isEmpty(param)) param = "?" + param;
				url = new URL(Constants.MAIN + jsonAction + param);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-Type", contentType);
				break;

			case REQUEST_TYPE_PUT:
				url = new URL(Constants.MAIN + jsonAction);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("PUT");
				break;

			case REQUEST_TYPE_PATCH:
				url = new URL(Constants.MAIN + jsonAction);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("PATCH");
				break;

			case REQUEST_TYPE_DELETE:
				url = new URL(Constants.MAIN + jsonAction);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("DELETE");
				break;
		}

		//Add headers
		if (headers != null && headers.size() > 0) {
            for(NameValuePair pair : headers){
                conn.setRequestProperty(pair.getName(), pair.getValue());
            }
        }
		if (!TextUtils.isEmpty(accept))
			conn.setRequestProperty("Accept", accept);

		if (requestType != REQUEST_TYPE_GET) {
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", contentType);
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));

			DataOutputStream wr = null;

			try {
				wr = new DataOutputStream(conn.getOutputStream());
				wr.write(postData);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (wr != null)
					try {
						wr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}

		// read the response
		InputStream in;
		int statusCode = conn.getResponseCode();
		if(statusCode == 500){
			in = conn.getErrorStream();
		}else{
			in = new BufferedInputStream(conn.getInputStream());
		}
		String strResponse = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

		Log.v("WsRequest response", strResponse);
		Log.v("WsRequest", "--------------------------------------------------------------------------------");
		Log.v("WsRequest", "--------------------------------------------------------------------------------");

		WsResponse wsResponse = new WsResponse(statusCode, strResponse);

		Log.i("WsRequest", jsonAction + " - Terminado");

		conn.disconnect();

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
			return null;
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