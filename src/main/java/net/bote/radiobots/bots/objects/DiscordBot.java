package net.bote.radiobots.bots.objects;
//
//import lombok.Getter;
//import net.bote.radiobots.bots.RadioBot;
//import net.bote.radiobots.bots.enums.RadioBotType;
//import net.bote.radiobots.request.RBRequest;
//import net.bote.radiobots.request.RadioBotStream;
//import net.bote.radiobots.util.MapBuilder;
//import net.bote.radiobots.util.MapPair;
//import net.bote.radiobots.util.RBAPIAuth;
//import org.json.JSONObject;
//
//import java.io.IOException;

/**
 * @author Elias Arndt | bote100
 * Created on 18.07.2019
 */

public class DiscordBot /*implements RadioBot */{

//    private final int uuid;
//    private String host;
//    private String name;
//
//    private int volume;
//    private JSONObject jsonObject;
//    private boolean success;
//    private RadioBotStream musicStream;
//    private String state;
//
//    // specific discord
//    @Getter private String prefix;
//    @Getter private int members;
//
//    @Getter
//    private final long guild;
//
//    @Getter
//    private final RBAPIAuth authentication;
//
//    public DiscordBot(RBAPIAuth rbapiAuth, int uuid, long guild) {
//        this.uuid = uuid;
//        this.authentication = rbapiAuth;
//        this.guild = guild;
//
//        refreshValues();
//
//    }
//
//    @Override
//    public RadioBotType getRadioBotType() {
//        return RadioBotType.DISCORD;
//    }
//
//    @Override
//    public String getServerName() {
//        return this.host;
//    }
//
//    @Override
//    public String getName() {
//        refreshValues();
//        return this.name;
//    }
//
//    @Override
//    public int getUUID() {
//        return this.uuid;
//    }
//
//    @Override
//    public int getVol() {
//        refreshValues();
//        return this.volume;
//    }
//
//    @Override
//    public void setVol(int level) {
//        if (level > 100 || level < 0) throw new IndexOutOfBoundsException("Volume must be between 1 and 100");
//
//        try {
//            RBRequest.request(getRadioBotType(), "volume",
//                    MapBuilder.buildStringMap(new MapPair("id", this.uuid), new MapPair("volume", level), new MapPair("guild", this.guild)), this.authentication);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        this.volume = level;
//    }
//
//    @Override
//    public void play(RBAPIAuth auth, String stream) {
//
//    }
//
//    @Override
//    public boolean isRunning() {
//        return state.equals("RUNNING");
//    }
//
//    @Override
//    public boolean isOwn() {
//        return this.success;
//    }
//
//    @Override
//    public RadioBotStream getRadioBotStream() {
//        return null;
//    }
//
//    public void refreshValues() {
//        try {
//            this.jsonObject = RBRequest.request(getRadioBotType(), "status", MapBuilder.buildStringMap(new MapPair("id", this.uuid), new MapPair("guild", this.guild)), this.authentication);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        this.success = this.jsonObject.getBoolean("success");
//        this.host = this.jsonObject.getString("server");
//        this.name = this.jsonObject.getString("nickname");
//        this.volume = this.jsonObject.getInt("volume");
//        this.state = this.jsonObject.getString("state");
//        this.prefix = this.jsonObject.getString("prefix");
//        this.members = this.jsonObject.getInt("members");
//    }

}
