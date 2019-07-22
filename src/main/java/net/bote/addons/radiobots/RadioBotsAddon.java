package net.bote.addons.radiobots;

import net.bote.addons.radiobots.listener.gui.BotUUIDNumberChangeListener;
import net.bote.addons.radiobots.listener.gui.RBLoginDataListener;
import net.bote.addons.radiobots.listener.ingame.RadioBotControlListener;
import net.bote.addons.radiobots.module.BotOnlineModule;
import net.bote.addons.radiobots.module.BotVolumeModule;
import net.bote.addons.radiobots.module.CurrentStreamModule;
import net.bote.radiobots.RDBots;
import net.bote.radiobots.bots.objects.MusicBot;
import net.bote.radiobots.login.RBLoginCallBack;
import net.bote.radiobots.login.RadioBotLogin;
import net.bote.radiobots.login.RadioBotsLoginSession;
import net.bote.radiobots.request.RBRequest;
import net.bote.radiobots.util.RBAPIAuth;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.NumberElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.settings.elements.StringElement;
import net.labymod.utils.Material;
import java.util.List;

/**
 * @author Elias Arndt | bote100
 * Created on 20.07.2019
 */
public class RadioBotsAddon extends LabyModAddon {

    private static RadioBotsAddon instance;

    private String apiToken;
    private String rbEmail;
    private String rbPassword;
    private int botUUID;
    private RadioBotsLoginSession session;
    public RBAPIAuth auth;

    public MusicBot currentBot;

    // Modules
    private BotOnlineModule botOnlineModule;
    private CurrentStreamModule currentStreamModule;
    private BotVolumeModule botVolumeModule;

    /**
     * Called when the addon gets disabled
     */
    @Override
    public void onDisable() { }


    /**
     * Called when the addon gets enabled
     */
    @Override
    public void onEnable() { instance = this; }

    /**
     * Called when this addon's config was loaded and is ready to use
     */
    @Override
    public void loadConfig() {
        this.rbEmail = getConfig().has("INTERFACE_EMAIL") ? getConfig().get("INTERFACE_EMAIL").getAsString() : "";
        this.rbPassword = getConfig().has("INTERFACE_PASSWORD") ? getConfig().get("INTERFACE_PASSWORD").getAsString() : "";
        this.apiToken = getConfig().has("API_TOKEN") ? getConfig().get("API_TOKEN").getAsString() : "";
        this.botUUID = getConfig().has("BOT_UUID") ? getConfig().get("BOT_UUID").getAsInt() : 0;

        if(this.botUUID != 0) {

            this.auth = new RBAPIAuth(this.rbEmail, this.apiToken);
            try {
                this.session = new RadioBotLogin(this.rbPassword, this.auth).login(new RBLoginCallBack() {

                    @Override
                    public void onFailure(String s) {
                        System.err.println(getPrefix() + "§4Your login credentials are wrong! => " + s);
                    }

                    @Override
                    public void onSuccess(RadioBotsLoginSession radioBotsLoginSession) {
                        System.out.println("Logged in to account! ("+radioBotsLoginSession.getUserid()+")");

                        currentBot = RDBots.getMusicBot(botUUID, auth, radioBotsLoginSession);

                        if(!currentBot.isOwn()) {
                            System.err.println(getPrefix() + "§4The bot is not your own or is not created with the given API token!");
                            return;
                        }

                        botOnlineModule = new BotOnlineModule(currentBot);
                        currentStreamModule = new CurrentStreamModule(currentBot);
                        botVolumeModule = new BotVolumeModule(currentBot);

                        getApi().registerModule(currentStreamModule);
                        getApi().registerModule(botOnlineModule);
                        getApi().registerModule(new BotVolumeModule(currentBot));

                        RBRequest.setDeveloperMode(true);

                        getApi().getEventManager().register(new RadioBotControlListener());

                        getApi().registerForgeListener(currentStreamModule);
                        getApi().registerForgeListener(botOnlineModule);
                        getApi().registerForgeListener(botVolumeModule);
                    }

                });
            } catch (Exception e) {
                getApi().displayMessageInChat(getPrefix() + "§4" + e.getMessage());
                e.printStackTrace();
            }

        }

    }

    public static RadioBotsAddon getInstance() {
        return instance;
    }

    /**
     * Called when the addon's ingame settings should be filled
     *
     * @param subSettings a list containing the addon's settings' elements
     */
    @Override
    protected void fillSettings(List<SettingsElement> subSettings) {

        NumberElement uuidChangeElement = new NumberElement("Bot UUID",
                new ControlElement.IconData(Material.REDSTONE_LAMP_OFF), this.botUUID);
        uuidChangeElement.addCallback(new BotUUIDNumberChangeListener(uuidChangeElement));

        // Adding to settings
        subSettings.add(new StringElement( "API Token", new ControlElement.IconData( Material.LEVER ),
                this.apiToken, new RBLoginDataListener(RBLoginDataListener.ElementDataType.API_TOKEN)));
        subSettings.add(new StringElement( "RadioBots Email", new ControlElement.IconData( Material.PAPER ),
                this.rbEmail, new RBLoginDataListener(RBLoginDataListener.ElementDataType.INTERFACE_EMAIL)));
        subSettings.add(new StringElement( "RadioBots Password", new ControlElement.IconData( Material.PACKED_ICE ),
                this.rbPassword, new RBLoginDataListener(RBLoginDataListener.ElementDataType.INTERFACE_PASSWORD)));
        subSettings.add(uuidChangeElement);
    }

    public String getApiToken() { return apiToken; }

    public String getRbEmail() { return rbEmail; }

    public String getRbPassword() { return rbPassword; }

    public BotVolumeModule getBotVolumeModule() { return botVolumeModule; }

    public CurrentStreamModule getCurrentStreamModule() { return currentStreamModule; }

    public BotOnlineModule getBotOnlineModule() { return botOnlineModule; }

    public void setApiToken(String apiToken) { this.apiToken = apiToken; }

    public void setSession(RadioBotsLoginSession session) { this.session = session; }

    public void setRbEmail(String rbEmail) { this.rbEmail = rbEmail; }

    public void setRbPassword(String rbPassword) { this.rbPassword = rbPassword; }

    public void setBotUUID(int botUUID) { this.botUUID = botUUID; }

    public int getBotUUID() { return botUUID; }

    public RadioBotsLoginSession getSession() { return session; }

    public RBAPIAuth getAuth() { return auth; }

    public MusicBot getCurrentBot() { return currentBot; }

    public String getPrefix() { return "§8RadioBotsEU §7» "; }

}
