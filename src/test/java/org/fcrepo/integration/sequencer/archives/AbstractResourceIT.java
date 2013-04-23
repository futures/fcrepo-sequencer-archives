
package org.fcrepo.integration.sequencer.archives;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractResourceIT {

    protected static final int SERVER_PORT = parseInt(getProperty("test.port",
            "8080"));

    protected static final String HOSTNAME = "localhost";

    protected static final String serverAddress = "http://" + HOSTNAME + ":" +
            SERVER_PORT;

    protected final PoolingClientConnectionManager connectionManager =
            new PoolingClientConnectionManager();

    protected static HttpClient client;

    public AbstractResourceIT() {
        connectionManager.setMaxTotal(MAX_VALUE);
        connectionManager.setDefaultMaxPerRoute(5);
        connectionManager.closeIdleConnections(3, SECONDS);
        client = new DefaultHttpClient(connectionManager);
    }

    protected int getStatus(final HttpUriRequest method)
            throws ClientProtocolException, IOException {
        return client.execute(method).getStatusLine().getStatusCode();
    }

    protected static HttpPost postObjMethod(final String pid) {
        return new HttpPost(serverAddress + "/rest/objects/" + pid);
    }

    protected static HttpPost postDSMethod(final String pid, final String ds,
            final File file) throws UnsupportedEncodingException {
        final HttpPost post =
                new HttpPost(serverAddress + "/rest/objects/" + pid +
                        "/datastreams/" + ds);
        post.setEntity(new FileEntity(file));
        return post;
    }
}
