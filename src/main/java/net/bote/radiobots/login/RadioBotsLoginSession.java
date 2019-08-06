package net.bote.radiobots.login;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Elias Arndt | bote100
 * Created on 18.07.2019
 */

@RequiredArgsConstructor
@Getter
public class RadioBotsLoginSession {

    private final String username;
    private final int userid;
    private final String session;
    private final boolean valid;
    private final RadioBotLogin radioBotLogin;

}
