package net.bote.radiobots.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.bote.radiobots.bots.RadioBot;

/**
 * @author Elias Arndt | bote100
 * Created on 17.07.2019
 */

@AllArgsConstructor
@Getter
public class RadioBotStream {

    @Setter
    private String url;
    private String position;
    private String lenght;
    private boolean paused;
    private boolean playing;
    private RadioBot bot;

    public void pause() {
        this.bot.pause();
    }

}
