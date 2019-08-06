package net.bote.radiobots.login;

/**
 * @author Elias Arndt | bote100
 * Created on 18.07.2019
 */

public interface RBLoginCallBack {

    void onFailure(String data);
    void onSuccess(RadioBotsLoginSession loginSession);

}
