package net.bote.addons.radiobots.module;

import net.bote.addons.radiobots.RadioBotsAddon;
import net.bote.radiobots.bots.objects.MusicBot;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Elias Arndt | bote100
 * Created on 20.07.2019
 */

public class BotOnlineModule extends SimpleModule {

    private String out = "Loading...";
    private MusicBot musicBot;
    private long nextPing = System.currentTimeMillis() + 1;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public BotOnlineModule(MusicBot musicBot) {
        this.musicBot = musicBot;
    }

    public MusicBot getMusicBot() {
        return musicBot;
    }

    @Override
    public String getDisplayName() {
        return "Bot's server";
    }

    public void setOut(String out) {
        this.out = out;
    }

    @Override
    public String getDisplayValue() {
        return out;
    }

    @Override
    public String getDefaultValue() {
        return "Loading...";
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.DIODE);
    }

    @Override
    public ModuleCategory getCategory() {
        return ModuleCategoryRegistry.CATEGORY_EXTERNAL_SERVICES;
    }

    @Override
    public void loadSettings() {

    }

    @Override
    public String getControlName() { return "Display current server"; }

    @Override
    public String getSettingName() {
        return "Bot server";
    }

    @Override
    public String getDescription() {
        return "Display where your radio bot currently stays.";
    }

    @Override
    public int getSortingId() {
        return 0;
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent e) {
        if (LabyMod.getInstance().isInGame() && this.nextPing < System.currentTimeMillis()) {
            this.nextPing = System.currentTimeMillis() + 25000;
            if (RadioBotsAddon.getInstance().getBotUUID() == 0) return;

            executorService.execute(() -> {
                if(!this.musicBot.isOwn()) {
                    out = "This is not your bot!";
                    return;
                }
                out = (musicBot.isRunning() ? musicBot.getServerName() : "Not online!");
            });
        }
    }

}
