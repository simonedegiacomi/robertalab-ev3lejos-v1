package de.fhg.iais.roberta.runtime.ev3.http;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class HTTPClient {

    public static void sendPOST(String message, String ulrString) {
        try {
            java.net.URL url = new URL(ulrString);
            HttpURLConnection req = (HttpURLConnection) url.openConnection();

            req.setRequestMethod("POST");
            req.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            req.setRequestProperty("Accept", "application/json");
            req.setDoOutput(true);
            req.setDoInput(false); // ignore the response

            JSONObject json = new JSONObject();
            json.put("data", message);

            OutputStream out = req.getOutputStream();
            out.write(json.toString().getBytes(StandardCharsets.UTF_8));
            out.close();

            req.disconnect();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

}
