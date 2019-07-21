package net.bote.addons.radiobots.listener.ingame;

import net.bote.addons.radiobots.RadioBotsAddon;
import net.bote.radiobots.RDBots;
import net.bote.radiobots.exception.RadioBotsException;
import net.labymod.api.events.MessageSendEvent;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Elias Arndt | bote100
 * Created on 20.07.2019
 */

public class RadioBotControlListener implements MessageSendEvent {

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public boolean onSend(String s) {

        if(s.startsWith("+rdbots")) {

            if(RadioBotsAddon.getInstance().getCurrentBot() == null) return false;
            if(!RadioBotsAddon.getInstance().getCurrentBot().isOwn()) {
                RadioBotsAddon.getInstance().getApi().displayMessageInChat(
                        RadioBotsAddon.getInstance().getPrefix() + "§7This is §enot your §7bot!"
                );
                return true;
            }

            String[] args = s.split(" ");

            executorService.execute(() -> {
                try {
                    switch (args.length) {
                        case 3:
                            if (args[1].equalsIgnoreCase("play")) {
                                RadioBotsAddon.getInstance().getCurrentBot().play(RadioBotsAddon.getInstance().getAuth(), args[2]);
                                RadioBotsAddon.getInstance().getApi().displayMessageInChat(RadioBotsAddon.getInstance().getPrefix() + "§7Switched music to §e" + args[2]);
                                RadioBotsAddon.getInstance().getCurrentStreamModule().setPaused(false);
                                RadioBotsAddon.getInstance().getCurrentStreamModule().setOut(
                                        (RadioBotsAddon.getInstance().getCurrentStreamModule().shouldShowTitle()) ? RDBots.getMusicTitle(args[2]) : args[2]);
                            } else if (args[1].equalsIgnoreCase("connect")) {
                                RadioBotsAddon.getInstance().getCurrentBot().changeServerAndConnect(args[2]);
                                RadioBotsAddon.getInstance().getApi().displayMessageInChat(
                                        RadioBotsAddon.getInstance().getPrefix() + "§7Bot is now connected to §e" + args[2]
                                );
                                RadioBotsAddon.getInstance().getBotOnlineModule().setOut(args[2]);
                            } else if (args[1].equalsIgnoreCase("volume")) {
                                try {
                                    int val = Integer.parseInt(args[2]);

                                    if (val > 100 || val < 1) {
                                        RadioBotsAddon.getInstance().getApi().displayMessageInChat(
                                                RadioBotsAddon.getInstance().getPrefix() + "§7Volume must be between §e1 and 100"
                                        );
                                        return;
                                    }

                                    RadioBotsAddon.getInstance().getBotVolumeModule().setOut(val + "%");
                                    RadioBotsAddon.getInstance().getCurrentBot().setVolume(val);
                                    RadioBotsAddon.getInstance().getApi().displayMessageInChat(
                                            RadioBotsAddon.getInstance().getPrefix() + "§7You changed the volume to §e" + val + "%"
                                    );

                                } catch (NumberFormatException ex) {
                                    RadioBotsAddon.getInstance().getApi().displayMessageInChat(
                                            RadioBotsAddon.getInstance().getPrefix() + "§7You must enter a §evalid number§7!"
                                    );
                                }

                            } else if(args[1].equalsIgnoreCase("rename")) {
                                String name = Arrays.stream(args, 2, args.length).collect(Collectors.joining());
                                RadioBotsAddon.getInstance().getCurrentBot().changeName(name);
                                RadioBotsAddon.getInstance().getApi().displayMessageInChat(
                                        RadioBotsAddon.getInstance().getPrefix() + "§7You §erenamed §7your bot §eto " + name
                                );
                                break;
                            }
                        case 2:
                            if (args[1].equals("start")) {
                                RadioBotsAddon.getInstance().getCurrentBot().start();
                                RadioBotsAddon.getInstance().getCurrentStreamModule().setOut(
                                        (RadioBotsAddon.getInstance().getCurrentStreamModule().shouldShowTitle()) ?
                                                RDBots.getMusicTitle(RadioBotsAddon.getInstance().getCurrentBot().getRadioBotStream().getUrl())
                                                : RadioBotsAddon.getInstance().getCurrentBot().getRadioBotStream().getUrl());
                                RadioBotsAddon.getInstance().getApi().displayMessageInChat(
                                        RadioBotsAddon.getInstance().getPrefix() + "§7Your bot is §anow online§7!"
                                );
                                RadioBotsAddon.getInstance().getBotOnlineModule().setOut(
                                        RadioBotsAddon.getInstance().getCurrentBot().getServerName()
                                );
                            } else if (args[1].equalsIgnoreCase("stop")) {
                                RadioBotsAddon.getInstance().getBotOnlineModule().setOut("Not online");
                                RadioBotsAddon.getInstance().getCurrentStreamModule().setOut("Plays nothing!");
                                RadioBotsAddon.getInstance().getCurrentBot().shutdown();
                                RadioBotsAddon.getInstance().getApi().displayMessageInChat(
                                        RadioBotsAddon.getInstance().getPrefix() + "§7Your bot is §cnow offline§7!"
                                );
                            } else if (args[1].equalsIgnoreCase("pause")) {
                                RadioBotsAddon.getInstance().getCurrentStreamModule().setOut("Music paused!");
                                RadioBotsAddon.getInstance().getCurrentStreamModule().setPaused(true);
                                RadioBotsAddon.getInstance().getCurrentBot().pause();
                                RadioBotsAddon.getInstance().getApi().displayMessageInChat(
                                        RadioBotsAddon.getInstance().getPrefix() + "§7The music of your §ebot §7is now §epaused§7!"
                                );
                            } else if (args[1].equalsIgnoreCase("resume")) {
                                RadioBotsAddon.getInstance().getCurrentStreamModule().setOut(
                                        (RadioBotsAddon.getInstance().getCurrentStreamModule().shouldShowTitle()) ?
                                                RDBots.getMusicTitle(RadioBotsAddon.getInstance().getCurrentBot().getRadioBotStream().getUrl())
                                                : RadioBotsAddon.getInstance().getCurrentBot().getRadioBotStream().getUrl());
                                RadioBotsAddon.getInstance().getCurrentBot().play(
                                        RadioBotsAddon.getInstance().getCurrentBot().getAuth(),
                                        RadioBotsAddon.getInstance().getCurrentBot().getRadioBotStream().getUrl()
                                );
                                RadioBotsAddon.getInstance().getApi().displayMessageInChat(
                                        RadioBotsAddon.getInstance().getPrefix() + "§7You §eresumed §7the music."
                                );
                                RadioBotsAddon.getInstance().getCurrentStreamModule().setPaused(false);
                            } else if (args[1].equalsIgnoreCase("help")) RadioBotControlListener.this.sendHelp();
                            break;
                        case 1:
                            RadioBotControlListener.this.sendHelp();
                            break;
                    }
                } catch (RadioBotsException ex) {
                    RadioBotsAddon.getInstance().getApi().displayMessageInChat(
                            RadioBotsAddon.getInstance().getPrefix() + "§cERROR: " + ex.getMessage()
                    );
                }
            });

            return true;
    }

        return false;
    }

    public void sendHelp() {
        RadioBotsAddon.getInstance().getApi().displayMessageInChat(RadioBotsAddon.getInstance().getPrefix() + "§7All commands:");
        RadioBotsAddon.getInstance().getApi().displayMessageInChat("§e+rdbots help §8| §7Show this help site");
        RadioBotsAddon.getInstance().getApi().displayMessageInChat("§e+rdbots start §8| §7Start bot");
        RadioBotsAddon.getInstance().getApi().displayMessageInChat("§e+rdbots stop §8| §7Shutdown bot");
        RadioBotsAddon.getInstance().getApi().displayMessageInChat("§e+rdbots pause §8| §7Pause the music");
        RadioBotsAddon.getInstance().getApi().displayMessageInChat("§e+rdbots resume §8| §7Resume the music");
        RadioBotsAddon.getInstance().getApi().displayMessageInChat("§e+rdbots play <URL> §8| §7Play a stream");
        RadioBotsAddon.getInstance().getApi().displayMessageInChat("§e+rdbots volume <1-100>§8| §7Switch the volume");
        RadioBotsAddon.getInstance().getApi().displayMessageInChat("§e+rdbots connect <Server-IP>§8| §7Connect to another server");
        RadioBotsAddon.getInstance().getApi().displayMessageInChat("§e+rdbots rename <New name>§8| §7Change the name of your bot");
    }

}
