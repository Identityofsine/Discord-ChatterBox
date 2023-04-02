package Audio.LavaPlayer;

import App.DiscordAPI;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DisconnectListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        super.onGuildVoiceUpdate(event);
        if(event.getMember().getIdLong() == DiscordAPI.getInstance().getJda().getSelfUser().getIdLong()){
            PlayerManager.get().stop(event.getGuild());
        }
    }


}
