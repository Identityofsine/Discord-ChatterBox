package Commands.Runnable;

import App.DiscordAPI;
import Audio.AudioCenter;
import Audio.AudioHandler;
import Audio.LavaPlayer.PlayerManager;
import Commands.CommandBehavior;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.self.GenericSelfUpdateEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.http.client.entity.InputStreamFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

public class Join extends CommandBehavior {

    private AudioPlayer createPlayer(){
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManager sourceManager = new LocalAudioSourceManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioPlayer player = playerManager.createPlayer();
        sourceManager.loadItem(playerManager,new AudioReference("src/main/java/assets/drakemoan.wav", "drakemoan"));

        return player;
    }


    @Override
    public void action(GenericEvent event) {

    }

    @Override
    public void action(GenericSelfUpdateEvent<?> event) {

    }

    @Override
    public void action(GenericUserEvent event) {

    }

    @Override
    public void action(GenericMessageEvent event) {
        JDA jda = DiscordAPI.getInstance().getJda();
        Member user = CommandBehavior.getMessageSender(event);
        VoiceChannel voiceChannel = user.getVoiceState().getChannel().asVoiceChannel();
        Guild guild = voiceChannel.getGuild();
//        var audioHandler = AudioCenter.getInstance().wavToAudioSendHandler(new File("src/main/java/assets/drakemoan.wav"));


        guild.getAudioManager().openAudioConnection(voiceChannel);
        PlayerManager audioHandler = PlayerManager.get();
        audioHandler.play(event.getGuild(), "https://www.youtube.com/watch?v=Liek9WZERGs");

        if(audioHandler.isPlaying()){
            String message = "Queue : \n";
            Iterator<AudioTrack> it = audioHandler.getGuildMusicManager(guild).getTrackScheduler().getQueue().iterator();
            int i = 1;
            while(it.hasNext()){
                message += String.format("%d: %s\n", i++, it.next().getInfo().title);
            }
            message += String.format("%d: %s\n", i, "28 Days Later (Little bit Slowed)");
            event.getChannel().sendMessage(message).queue();
        }
        else
            event.getChannel().sendMessage("Playing " + "28 Days Later (Little bit Slowed)").queue();



    }

    @Override
    public void action(GenericRoleEvent event) {

    }
}
