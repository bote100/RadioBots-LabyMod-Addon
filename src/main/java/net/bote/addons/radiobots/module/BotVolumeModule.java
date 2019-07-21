package net.bote.addons.radiobots.module;

import net.bote.addons.radiobots.RadioBotsAddon;
import net.bote.radiobots.RDBots;
import net.bote.radiobots.bots.objects.MusicBot;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Elias Arndt | bote100
 * Created on 20.07.2019
 */

public class BotVolumeModule extends SimpleModule {

    private MusicBot musicBot;
    private long nextPing = System.currentTimeMillis() + 1;
    private String out = "Loading...";
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public BotVolumeModule(MusicBot musicBot) {
        this.musicBot = musicBot;
    }

    @Override
    public String getDisplayName() {
        return "Bot volume";
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
        return new ControlElement.IconData(Material.DISPENSER);
    }

    @Override
    public void loadSettings() { }

    @Override
    public String getSettingName() {
        return "volume_module";
    }

    @Override
    public String getDescription() {
        return "Display the volume of your bot.";
    }

    @Override
    public String getControlName() {
        return "Display bot volume";
    }

    @Override
    public int getSortingId() {
        return 0;
    }

    public void setOut(String out) {
        this.out = out;
    }

    @Override
    public ModuleCategory getCategory() {
        return ModuleCategoryRegistry.CATEGORY_EXTERNAL_SERVICES;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (LabyMod.getInstance().isInGame() && this.nextPing < System.currentTimeMillis()) {
            this.nextPing = System.currentTimeMillis() + 25000;
            if (RadioBotsAddon.getInstance().getBotUUID() == 0) return;
            executorService.execute(() -> out = BotVolumeModule.this.musicBot.getVolume() + "%");
        }
    }

}
