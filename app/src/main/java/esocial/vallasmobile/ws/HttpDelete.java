package esocial.vallasmobile.ws;

import org.apache.http.client.methods.HttpPost;

import java.net.URI;

public class HttpDelete extends HttpPost {
	public final static String METHOD_NAME = "DELETE";

    public HttpDelete() {
        super();
    }

    public HttpDelete(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpDelete(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}