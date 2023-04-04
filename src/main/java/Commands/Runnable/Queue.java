package Commands.Runnable;

import App.Util;
import Audio.LavaPlayer.PlayerManager;
import Commands.CommandBehavior;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.self.GenericSelfUpdateEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import javax.sound.midi.Track;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Queue extends CommandBehavior {
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
        PlayerManager getPlayer = PlayerManager.get();
        if(getPlayer.isPlaying()){
            AudioTrack track = getPlayer.getGuildMusicManager(event.getGuild()).getTrackScheduler().getCurrentTrack();
            String currentTrack = track.getInfo().title;
            StringBuilder bulk_message = new StringBuilder("\nI'm currently playing : " + currentTrack + " (" + PlayerManager.formatDurationToMMSS(track.getDuration()) + ")\n");
            bulk_message = new StringBuilder(MarkdownUtil.bold(bulk_message.toString()));
            bulk_message.append(Util.repeatString("-", currentTrack.length())).append("\n");
            BlockingQueue<AudioTrack> queue = getPlayer.getGuildMusicManager(event.getGuild()).getTrackScheduler().getQueue();
            if (queue.isEmpty()){
                bulk_message.append(MarkdownUtil.bold("*Nothing is queued at the moment...*\n" + currentTrack));
            }
            else{
                int i = 1;
                for (AudioTrack audioTrack : queue) {
                    String formattedDuration = PlayerManager.formatDurationToMMSS(audioTrack.getDuration());

                    bulk_message.append(String.format("**%d :** %s (%s)\n", i++, MarkdownUtil.bold(audioTrack.getInfo().title), formattedDuration));
                }
            }
            event.getChannel().sendMessage(bulk_message.toString()).complete();
        }
        else{
            event.getChannel().sendMessage(MarkdownUtil.bold("I'm not playing anything right now...")).complete();
        }
    }

    @Override
    public void action(GenericRoleEvent event) {

    }
}
