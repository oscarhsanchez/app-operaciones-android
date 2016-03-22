package esocial.vallasmobile.ws;


public class WsResponse {

	public Boolean success;
	private int statusCode;
	private String result;
	public esocial.vallasmobile.obj.Error error;
	
	public WsResponse() {	}
	
	public WsResponse(int statusCode, String result) {
		super();
		this.statusCode = statusCode;
		this.result = result;
	}

	public int getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}


	public boolean failed() {
		return (statusCode!=200 && statusCode!=201) || error!=null;
	}
	
}
