package Commands.Runnable;

import Audio.LavaPlayer.AudioBehavior;
import Audio.LavaPlayer.PlayerManager;
import Commands.Arguments.Argument;
import Commands.CommandBehavior;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.self.GenericSelfUpdateEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

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

    protected boolean isLink(String arg){
        return arg.matches("/((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)/\n");
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
            return;
        }
        String url = "";
        try {
            url = arr[1];
        }
        catch(ArrayIndexOutOfBoundsException e){
            event.getChannel().sendMessage("Missing Youtube URL").queue();
            return;
        }
        if(!isLink(url)){
            event.getChannel().sendMessage("Missing Youtube URL").queue();
            return;
        }
        this.getArgument("url").setValue(url);
        Member user = CommandBehavior.getMessageSender(event);
        try{
            VoiceChannel voiceChannel = user.getVoiceState().getChannel().asVoiceChannel();

            Guild guild = voiceChannel.getGuild();


            PlayerManager audioHandler = PlayerManager.get();
            guild.getAudioManager().openAudioConnection(voiceChannel);

            audioHandler.play(event.getGuild(), (String) this.getArgument("url").getValue(), new AudioBehavior() {
                @Override
                public void queueBehavior(AudioTrack track) {
                    event.getChannel().sendMessage(MarkdownUtil.bold(String.format("Now Playing : *%s* (%s)", track.getInfo().title, PlayerManager.formatDurationToMMSS(track.getDuration())))).queue();
                }

                @Override
                public void onLoadBehavior(AudioTrack track) {
                    if(!audioHandler.isPlaying())
                        event.getChannel().sendMessage(MarkdownUtil.bold(String.format("Now Playing : *%s* (%s)", track.getInfo().title, PlayerManager.formatDurationToMMSS(track.getDuration())))).queue();
                    else
                        event.getChannel().sendMessage(MarkdownUtil.bold(String.format("Added \"*%s*\" (%s) to queue!", track.getInfo().title, PlayerManager.formatDurationToMMSS(track.getDuration())))).queue();
                }

                @Override
                public void endBehavior(AudioTrack track) {
                    guild.getAudioManager().closeAudioConnection();
                }
            });
        }
        catch (NullPointerException e){
            event.getChannel().sendMessage("You aren't in a voice channel! **(or I can't join...)**").complete();
        }
    }

    @Override
    public void action(GenericRoleEvent event) {

    }
}
