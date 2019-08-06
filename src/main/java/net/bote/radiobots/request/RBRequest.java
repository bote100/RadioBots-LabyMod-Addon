package net.bote.radiobots.request;

import lombok.Getter;
import lombok.Setter;
import net.bote.radiobots.bots.enums.RadioBotType;
import net.bote.radiobots.exception.MissingAPIParameterException;
import net.bote.radiobots.exception.RadioBotsException;
import net.bote.radiobots.util.MapBuilder;
import net.bote.radiobots.util.MapPair;
import net.bote.radiobots.util.RBAPIAuth;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * @author Elias Arndt | bote100
 * Created on 17.07.2019
 */

public class RBRequest {

    @Getter
    @Setter
    public static boolean developerMode = false;

    public static JSONObject request(RadioBotType type, String service, Map<String, String> params, RBAPIAuth auth) throws IOException {

        HttpGet httpGet = new HttpGet("https://api.radiobots.eu/v3/" + type.toString().toLowerCase() + "/bot/" + service + ".php");

        JSONObject requestData = new JSONObject();
        params.keySet().forEach(key -> requestData.put(key, params.get(key)));
        httpGet.setHeader("data", requestData.toString());

        httpGet.setHeader("auth", "{\"user\":\""+auth.getUser()+"\",\"token\":\""+auth.getToken()+"\"}");

        CloseableHttpClient client = HttpClientBuilder.create().setUserAgent("PostmanRuntime/7.15.0").build();

        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else throw new ClientProtocolException("Unexpected response status: " + status);
        };
        String responseBody = client.execute(httpGet, responseHandler);
        if(responseBody.charAt(0) != '{') responseBody = responseBody.substring(1);

        if(responseBody.charAt(0) != '{') return null;

        if(developerMode) System.out.println(responseBody);

        try {
            JSONObject jsonObject = new JSONObject(responseBody);

            if(!jsonObject.isNull("success"))
                if(!jsonObject.getBoolean("success")) {
                    String data = jsonObject.getString("data");
                    switch (data) {
                        case "The Bot is not yours.":
                            throw new SecurityException(jsonObject.getString("data") + " (or is not created with the current api token!)");
                    }
                    if(data.contains("You have reached your daily Quota")) throw new RadioBotsException(data);
                    else if(data.contains("Empty field")) throw new MissingAPIParameterException(data);
                    else if(data.contains("Bot already online")) throw new RadioBotsException(data);
                    else if(data.contains("Server down")) throw new RadioBotsException("The RadioBots-Software on the selected server is currently down...");
                    else if(data.contains("authentication failed")) throw new RadioBotsException(data);

                }

            client.close();
            return jsonObject;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static JSONObject request(RadioBotType type, String service, int id, RBAPIAuth auth) throws IOException {
        return request(type, service, MapBuilder.buildStringMap(new MapPair("id", String.valueOf(id))), auth);
    }

}
