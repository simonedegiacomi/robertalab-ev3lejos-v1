package de.fhg.iais.roberta.runtime.ev3.http;

import fi.iki.elonen.NanoHTTPD;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class HTTPServer extends NanoHTTPD {

    public static final String NO_MESSAGE = "NO MESSAGE";
    public static final String MESSAGE_ERROR = "MESSAGE ERROR";

    private String receivedMessage = NO_MESSAGE;
    private final CountDownLatch latch = new CountDownLatch(1);
    private final String path;

    private HTTPServer(String hostname, int port, String path) {
        super(hostname, port);
        this.path = path;
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            if (session.getUri().equals(path)) {
                String body = getBodyAsString(session);
                JSONObject json = new JSONObject(body);
                receivedMessage = json.optString("message");
                latch.countDown();
            }
        } catch ( Exception e ) {
            receivedMessage = MESSAGE_ERROR;
            latch.countDown();
        }
        return newFixedLengthResponse("ok");
    }

    public static String readMessageAndClose(String urlString) {
        try {
            URL url = new URL(urlString);
            HTTPServer server = new HTTPServer(url.getHost(), url.getPort(), url.getPath());

            server.start();
            server.latch.await();
            server.stop();
            return server.receivedMessage;
        } catch ( Exception e ) {
            return MESSAGE_ERROR;
        }
    }

    private String getBodyAsString(IHTTPSession session) throws IOException {
        int contentLength = Integer.parseInt(session.getHeaders().get("content-length"));
        byte[] buffer = new byte[contentLength];
        session.getInputStream().read(buffer, 0, contentLength);
        return new String(buffer, StandardCharsets.UTF_8);
    }

}
