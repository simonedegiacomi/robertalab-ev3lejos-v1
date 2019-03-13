package de.fhg.iais.roberta.runtime.ev3.http;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class HTTPClient {

    /**
     * Send a POST request to the specified URL with the message inside a JSON Object
     * @param message
     * @param ulrString
     */
    public static void sendPOST(String message, String ulrString) {
        JSONObject json = new JSONObject();
        json.put("message", message);
        byte[] bytesToSend = json.toString().getBytes(StandardCharsets.UTF_8);

        try {
            URL url = new URL(ulrString);
            HttpURLConnection req = (HttpURLConnection) url.openConnection();

            req.setRequestMethod("POST");
            req.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            req.setRequestProperty("Content-Length", String.valueOf(bytesToSend.length));
            req.setRequestProperty("Accept", "application/json");
            req.setDoOutput(true);
            req.setDoInput(true);
            req.connect();

            OutputStream out = req.getOutputStream();
            out.write(bytesToSend);
            out.close();

            InputStream in = req.getInputStream();
            in.close();

            req.disconnect();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

}
