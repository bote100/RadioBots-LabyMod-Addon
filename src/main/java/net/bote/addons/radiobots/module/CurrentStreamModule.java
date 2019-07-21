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
import net.labymod.utils.Consumer;
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

public class CurrentStreamModule extends SimpleModule {

    private MusicBot musicBot;
    private long nextPing = System.currentTimeMillis() + 1;
    private String out = "Loading...";
    private ExecutorService executorService = Executors.newCachedThreadPool();

    private boolean paused = false;
    private boolean showTitle = true;

    public CurrentStreamModule(MusicBot musicBot) {
        this.musicBot = musicBot;
    }

    @Override
    public String getDisplayName() {
        return "Stream";
    }

    @Override
    public String getDisplayValue() {
        return out;
    }

    @Override
    public String getDefaultValue() {
        return "Plays nothing!";
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.JUKEBOX);
    }

    @Override
    public void fillSubSettings(List<SettingsElement> settingsElements) {
        settingsElements.add( new BooleanElement( "Show title", new ControlElement.IconData( Material.NAME_TAG ), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean accepted) {
                RadioBotsAddon.getInstance().getConfig().addProperty("SHOW_TITLE", accepted);
                CurrentStreamModule.this.showTitle = accepted;
                executorService.execute(() -> out = (CurrentStreamModule.this.musicBot.getRadioBotStream().isPlaying()) ?
                        (CurrentStreamModule.this.showTitle ? RDBots.getMusicTitle(CurrentStreamModule.this.musicBot.getRadioBotStream().getUrl()) :
                                CurrentStreamModule.this.musicBot.getRadioBotStream().getUrl()) : "Plays nothing!");
            }
        }, (RadioBotsAddon.getInstance().getConfig().has("SHOW_TITLE") ? RadioBotsAddon.getInstance().getConfig().get("SHOW_TITLE").getAsBoolean() : true)));
    }

    @Override
    public void loadSettings() {

    }

    @Override
    public String getSettingName() {
        return "stream_module";
    }

    @Override
    public String getDescription() {
        return "Display your current stream played by your bot.";
    }

    @Override
    public String getControlName() {
        return "Display playing stream";
    }

    @Override
    public int getSortingId() {
        return 0;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean shouldShowTitle() {
        return showTitle;
    }

    @Override
    public ModuleCategory getCategory() {
        return ModuleCategoryRegistry.CATEGORY_EXTERNAL_SERVICES;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (LabyMod.getInstance().isInGame() && this.nextPing < System.currentTimeMillis()) {

            if(isPaused()) out = "Music paused!";

            this.nextPing = System.currentTimeMillis() + 25000;
            if (RadioBotsAddon.getInstance().getBotUUID() == 0) return;

            executorService.execute(() -> out = (CurrentStreamModule.this.musicBot.getRadioBotStream().isPlaying()) ?
                    (CurrentStreamModule.this.showTitle ? RDBots.getMusicTitle(CurrentStreamModule.this.musicBot.getRadioBotStream().getUrl()) :
                            CurrentStreamModule.this.musicBot.getRadioBotStream().getUrl()) : "Plays nothing!");
        }
    }

}
