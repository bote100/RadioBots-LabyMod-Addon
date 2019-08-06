package net.bote.radiobots.request;

import lombok.Getter;
import lombok.ToString;
import net.bote.radiobots.bots.objects.MusicBot;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Elias Arndt | bote100
 * Created on 18.07.2019
 */

@Getter
@ToString
public class BotSettings {

    private String address;
    private int userid;
    private Date creation;
    private String channelPassword;
    private String serverPassword;

    private final MusicBot musicBot;

    public BotSettings(MusicBot bot) {
        this.musicBot = bot;
        refreshValues();
    }

    public void refreshValues() {
        try {

            JSONObject jsonObject = RBRequest.request(this.musicBot.getRadioBotType(), "get-settings", this.musicBot.getUUID(), this.musicBot.getAuth());

            this.address = jsonObject.getString("adress");
            this.userid = jsonObject.getInt("userid");
            this.channelPassword = jsonObject.getString("channelpw");
            this.serverPassword = jsonObject.getString("serverpw");
            try {
                this.creation = new SimpleDateFormat("yyyy-L-d HH:mm:ss").parse(jsonObject.getString("creation"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
