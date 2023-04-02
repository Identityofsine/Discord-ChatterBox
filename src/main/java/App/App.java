package App;

import Audio.LavaPlayer.DisconnectListener;
import Commands.CommandHandler;
import Commands.CommandInit;

public class App {

    public static void main(String[] args) {
        DiscordAPI discord = DiscordAPI.getInstance();
        discord.addListener(new CommandHandler());
        discord.addListener(new DisconnectListener());
        CommandInit.addChatCommands();
    }
}
