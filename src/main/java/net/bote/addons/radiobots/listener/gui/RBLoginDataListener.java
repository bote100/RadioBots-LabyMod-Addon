package net.bote.addons.radiobots.listener.gui;

import net.bote.addons.radiobots.RadioBotsAddon;
import net.bote.radiobots.login.RadioBotLogin;
import net.bote.radiobots.login.RadioBotsLoginSession;
import net.labymod.settings.elements.StringElement;
import net.labymod.utils.Consumer;

/**
 * @author Elias Arndt | bote100
 * Created on 20.07.2019
 */

public class RBLoginDataListener implements Consumer<String> {

    private ElementDataType elementDataType;

    public RBLoginDataListener(ElementDataType elementDataType) {
        this.elementDataType = elementDataType;
    }

    public ElementDataType getElementDataType() {
        return elementDataType;
    }

    @Override
    public void accept(String s) {
        RadioBotsAddon.getInstance().getConfig().addProperty(this.elementDataType.toString(), s);
    }

    public enum ElementDataType {
        API_TOKEN,
        API_USERNAME,
        INTERFACE_EMAIL,
        INTERFACE_PASSWORD;
    }

}
