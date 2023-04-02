package Commands.Runnable;

import Audio.LavaPlayer.PlayerManager;
import Commands.Arguments.Argument;
import Commands.CommandBehavior;
import com.sun.jdi.connect.Connector;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.self.GenericSelfUpdateEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;

class YoutubeInfo{

}


public class Play extends CommandBehavior {
    //will return null if not a url
    private YoutubeInfo grabYoutubeStats(String url){
        return null;
    }

    public Play(){
        this.addArgument(new Argument<String>("url", "Link"));
    }

    @Override
    protected void action(GenericEvent event) {

    }

    @Override
    public void action(GenericSelfUpdateEvent<?> event) {

    }

    @Override
    public void action(GenericUserEvent event) {

    }

    @Override
    public void action(GenericMessageEvent event) {
        String messageID = event.getMessageId();
        Message msg = event.getChannel().retrieveMessageById(messageID).complete();
        String msg_String = msg.getContentRaw();
        String[] arr = msg_String.split(" ");
        if(arr.length <= 1) {
            event.getChannel().sendMessage("Missing Youtube URL").queue();
        }
        String url = arr[1];
        this.getArgument("url").setValue(url);
        Member user = CommandBehavior.getMessageSender(event);
        VoiceChannel voiceChannel = user.getVoiceState().getChannel().asVoiceChannel();
        Guild guild = voiceChannel.getGuild();


        guild.getAudioManager().openAudioConnection(voiceChannel);
        var audioHandler = PlayerManager.get();
        audioHandler.play(event.getGuild(), (String)this.getArgument("url").getValue(), (item) -> {
            event.getChannel().sendMessage("Now Playing : " + item.getInfo().title).complete();
        });

        if(audioHandler.isPlaying()){
            String message = "Queue : \n";
            var it = audioHandler.getGuildMusicManager(guild).getTrackScheduler().getQueue().iterator();
            int i = 1;
            while(it.hasNext()){
                message += String.format("%d: %s\n", i++, it.next().getInfo().title);
            }
            message += String.format("%d: %s\n", i, url);
            event.getChannel().sendMessage(message).queue();
        }
        else
            event.getChannel().sendMessage("Playing " + url).queue();

    }

    @Override
    public void action(GenericRoleEvent event) {

    }
}
