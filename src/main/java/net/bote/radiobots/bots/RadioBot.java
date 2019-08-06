package net.bote.radiobots.bots;

import net.bote.radiobots.bots.enums.RadioBotType;
import net.bote.radiobots.request.RadioBotStream;
import net.bote.radiobots.util.RBAPIAuth;

/**
 * @author Elias Arndt | bote100
 * Created on 17.07.2019
 */

public interface RadioBot {

    RadioBotType getRadioBotType();

    String getServerName();

    String getName();

    int getUUID();

    int getVolume();

    void setVolume(int level);

    void seek(String pos);

    void play(RBAPIAuth auth, String stream);

    void pause();

    boolean isRunning();

    boolean isOwn();

    RadioBotStream getRadioBotStream();

    RBAPIAuth getAuth();

}
