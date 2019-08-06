package net.bote.radiobots;

import net.bote.radiobots.bots.enums.MusicBotLocation;
import net.bote.radiobots.bots.enums.RadioBotType;
import net.bote.radiobots.bots.objects.MusicBot;
import net.bote.radiobots.login.RadioBotsLoginSession;
import net.bote.radiobots.request.RBRequest;
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
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Elias Arndt | bote100
 * Created on 17.07.2019
 */

public class RDBots {

    private static HashMap<Integer, MusicBot> musicBotCache = new HashMap<>();

    public static MusicBot getMusicBot(int uuid, RBAPIAuth auth, RadioBotsLoginSession session) {
        if(musicBotCache.containsKey(uuid)) return musicBotCache.get(uuid);
        MusicBot musicBot = new MusicBot(uuid, auth, session);
        musicBotCache.put(uuid, musicBot);
        return musicBot;
    }

    public static MusicBot createMusicBot(RBAPIAuth auth, String nickname, String host, String serverpassword, MusicBotLocation location, RadioBotsLoginSession session) throws IOException {
        JSONObject callback = RBRequest.request(RadioBotType.TEAMSPEAK, "order", MapBuilder.buildStringMap(
                new MapPair("nick", nickname),
                new MapPair("ip", host),
                new MapPair("pw", serverpassword),
                new MapPair("location", location.toString().toUpperCase()),
                new MapPair("userid", String.valueOf(session.getUserid()))
                ), auth
        );

        if(!callback.getBoolean("success")) throw new RuntimeException(callback.getString("data"));

        return getMusicBot(callback.getInt("id"), auth, session);
    }

    public static String getMusicTitle(String url) {
        HttpGet httpGet = new HttpGet("https://3.radiobots.eu/php/bots/ts3.php?action=titledata&url=" + url);

        CloseableHttpClient client = HttpClientBuilder.create().setUserAgent("PostmanRuntime/7.15.0").build();

        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else throw new ClientProtocolException("Unexpected response status: " + status);
        };
        try {
            return client.execute(httpGet, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

}
