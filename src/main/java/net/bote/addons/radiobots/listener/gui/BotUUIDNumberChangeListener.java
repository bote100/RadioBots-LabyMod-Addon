package net.bote.addons.radiobots.listener.gui;

import net.bote.addons.radiobots.RadioBotsAddon;
import net.labymod.settings.elements.NumberElement;
import net.labymod.utils.Consumer;

/**
 * @author Elias Arndt | bote100
 * Created on 20.07.2019
 */

public class BotUUIDNumberChangeListener implements Consumer<Integer> {

    private NumberElement numberElement;

    public BotUUIDNumberChangeListener(NumberElement numberElement) {
        this.numberElement = numberElement;
    }

    @Override
    public void accept(Integer integer) {

        RadioBotsAddon.getInstance().getConfig().addProperty("BOT_UUID", integer);
        RadioBotsAddon.getInstance().setBotUUID(integer);

    }

    public NumberElement getNumberElement() {
        return numberElement;
    }
}
