package net.bote.radiobots.bots.objects;

import lombok.Getter;
import lombok.ToString;
import net.bote.radiobots.bots.RadioBot;
import net.bote.radiobots.bots.enums.RadioBotType;
import net.bote.radiobots.login.RadioBotsLoginSession;
import net.bote.radiobots.request.BotSettings;
import net.bote.radiobots.request.RBRequest;
import net.bote.radiobots.request.RadioBotStream;
import net.bote.radiobots.util.MapBuilder;
import net.bote.radiobots.util.MapPair;
import net.bote.radiobots.util.RBAPIAuth;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author Elias Arndt | bote100
 * Created on 17.07.2019
 */

@ToString
public class MusicBot implements RadioBot {

    private final int uuid;
    private String host;
    private String name;

    @Getter
    private boolean isCommander;

    private int vol;
    private boolean online;
    private JSONObject jsonObject;
    private boolean success;
    private RadioBotStream musicStream;
    private final RBAPIAuth authentication;

    @Getter
    private BotSettings settings;

    @Getter
    private final RadioBotsLoginSession loginSession;

    public MusicBot(int uuid, RBAPIAuth auth, RadioBotsLoginSession session) {
        this.uuid = uuid;
        this.authentication = auth;
        this.loginSession = session;

        refreshAll();
    }

    public RadioBotType getRadioBotType() {
        return RadioBotType.TEAMSPEAK;
    }

    public String getServerName() {
        return this.host;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int getUUID() {
        return this.uuid;
    }

    public int getVolume() {
        return this.vol;
    }

    @Override
    public void setVolume(int level) {

        if (level > 100 || level < 0) throw new IndexOutOfBoundsException("Volume can only be a number from 0 to 100");

        try {
            RBRequest.request(RadioBotType.TEAMSPEAK, "volume",
                    MapBuilder.buildStringMap(new MapPair("id", this.uuid), new MapPair("volume", level)), this.authentication);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.vol = level;
    }

    @Override
    public void seek(String pos) {
        try {
            RBRequest.request(RadioBotType.TEAMSPEAK, "seek", MapBuilder.buildStringMap(

                    new MapPair("ps", String.valueOf(this.uuid)),
                    new MapPair("pos", pos),
                    new MapPair("id", this.uuid)

            ), this.authentication);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(RBAPIAuth auth, String stream) {
        try {
            RBRequest.request(RadioBotType.TEAMSPEAK, "play", MapBuilder.buildStringMap(

                    new MapPair("id", String.valueOf(this.uuid)),
                    new MapPair("url", stream)

            ), this.authentication);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        try {
            RBRequest.request(RadioBotType.TEAMSPEAK, "pause", this.uuid, this.authentication);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            RBRequest.request(RadioBotType.TEAMSPEAK, "start", this.uuid, this.authentication);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            RBRequest.request(RadioBotType.TEAMSPEAK, "stop", this.uuid, this.authentication);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void playPlaylist(int id) {
        try {
            RBRequest.request(RadioBotType.TEAMSPEAK, "stop", MapBuilder.buildStringMap(new MapPair("id", this.uuid), new MapPair("playlistid", id)) , this.authentication);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteInstantly() {
        try {
            RBRequest.request(RadioBotType.TEAMSPEAK, "delete", this.uuid , this.authentication);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeName(String newName) {
        try {
            RBRequest.request(RadioBotType.TEAMSPEAK, "change-settings", MapBuilder.buildStringMap(
                    new MapPair("id", this.uuid),
                    new MapPair("name", newName)
            ) , this.authentication);
            this.name = newName;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeServerName(String newServerName) {
        try {
            RBRequest.request(RadioBotType.TEAMSPEAK, "change-settings", MapBuilder.buildStringMap(
                    new MapPair("id", this.uuid),
                    new MapPair("ip", newServerName)
            ) , this.authentication);
            this.host = newServerName;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeServerAndConnect(String newServerName) {
        boolean isPlaying = getRadioBotStream().isPlaying();
        changeServerName(newServerName);
        shutdown();
        start();
        if(isPlaying) play(authentication, getRadioBotStream().getUrl());
    }

    public boolean setCommander() {

        try {
            JSONObject jsonObject = RBRequest.request(RadioBotType.TEAMSPEAK, "commander", this.uuid, this.authentication);

            if (!jsonObject.getBoolean("success")) {
                System.err.println(jsonObject.getString("data"));
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.isCommander = true;
        return true;
    }

    public void refreshStream() {
        try {
            JSONObject streamRequest = RBRequest.request(RadioBotType.TEAMSPEAK, "song", this.uuid, authentication);
            System.out.println("streamRequest = " + streamRequest);
            this.musicStream = new RadioBotStream(getValOrNull("title", streamRequest), getValOrNull("position", streamRequest),
                    getValOrNull("Length", streamRequest), getBoolOrNull("paused", streamRequest), getBoolOrNull("playing", streamRequest), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getValOrNull(String key, JSONObject object) {
        if(object.isNull(key)) return null;
        return object.getString(key);
    }

    private boolean getBoolOrNull(String key, JSONObject object) {
        if(object.isNull(key)) return false;
        return object.getBoolean(key);
    }

    public void refreshValues() {
        try {
            this.jsonObject = RBRequest.request(getRadioBotType(), "status", uuid, this.authentication);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.success = this.jsonObject.getBoolean("success");
        this.host = this.jsonObject.getString("server");
        this.isCommander = (!this.jsonObject.isNull("commander") && this.jsonObject.getBoolean("commander"));
        this.name = this.jsonObject.getString("nickname");
        this.vol = this.jsonObject.getInt("volume");
        this.online = this.jsonObject.getBoolean("running");
        this.settings = new BotSettings(this);
    }

    public void refreshAll() {
        refreshValues();
        refreshStream();
    }

    public boolean isRunning() {
        return this.online;
    }

    public boolean isOwn() {
        return this.success;
    }

    @Override
    public RadioBotStream getRadioBotStream() {
        return this.musicStream;
    }

    @Override
    public RBAPIAuth getAuth() {
        return this.authentication;
    }
}
