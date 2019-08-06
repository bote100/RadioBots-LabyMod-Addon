package net.bote.radiobots.login;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
import java.util.Objects;

/**
 * @author Elias Arndt | bote100
 * Created on 18.07.2019
 */

@Getter
@RequiredArgsConstructor
public class RadioBotLogin {

    private final String password;
    private final RBAPIAuth rbapiAuth;

    public RadioBotsLoginSession login(RBLoginCallBack loginCallBack) throws IOException {
        JSONObject jsonObject = requestData(MapBuilder.buildStringMap(new MapPair("mail", this.rbapiAuth.getUser()), new MapPair("password", this.password)), this.rbapiAuth);

        if(!Objects.requireNonNull(jsonObject).getBoolean("success")) {
            loginCallBack.onFailure(jsonObject.getString("data"));
            return new RadioBotsLoginSession(null, -1, null, false, null);
        }

        RadioBotsLoginSession session = new RadioBotsLoginSession(jsonObject.getString("username"), jsonObject.getInt("userid"), jsonObject.getString("session"), jsonObject.getBoolean("success"), this);
        loginCallBack.onSuccess(session);

        return session;
    }

    public String getEmail() {
        return rbapiAuth.getUser();
    }

    private JSONObject requestData(Map<String, String> params, RBAPIAuth auth) throws IOException {

        HttpGet httpGet = new HttpGet("https://api.radiobots.eu/v3/utils/account/login.php");

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

        try {
            client.close();
            return new JSONObject(responseBody);
        } catch (JSONException ignored) { return null; }

    }

}
