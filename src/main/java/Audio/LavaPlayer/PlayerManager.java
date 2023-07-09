package Audio.LavaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlayerManager {

    private static PlayerManager INSTANCE;

    private boolean isPlaying;
    private Map<Long, GuildMusicManager> guildMusicManagers = new HashMap<>();
    private AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

    private PlayerManager() {
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
        isPlaying = false;
    }

    public static PlayerManager get() {
        if(INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }

    protected void setIsPlaying(boolean b){
        isPlaying = b;
    }

    public boolean isPlaying(){
        return isPlaying;
    }

    public GuildMusicManager getGuildMusicManager(Guild guild, AudioBehavior behavior) {
        return guildMusicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager musicManager = new GuildMusicManager(audioPlayerManager, guild, behavior);

            guild.getAudioManager().setSendingHandler(musicManager.getAudioForwarder());

            return musicManager;
        });
    }

    public GuildMusicManager getGuildMusicManager(Guild guild) {
        return guildMusicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager musicManager = new GuildMusicManager(audioPlayerManager, guild, new AudioBehavior() {
                @Override
                public void queueBehavior(AudioTrack track) {
                    //empty
                }

                @Override
                public void onLoadBehavior(AudioTrack track) {
                    //empty
                }

                @Override
                public void endBehavior(AudioTrack track) {
                    //empty
                }
            });

            guild.getAudioManager().setSendingHandler(musicManager.getAudioForwarder());

            return musicManager;
        });
    }

    public void stop(Guild guild){
        GuildMusicManager guildMusicManager = getGuildMusicManager(guild);
        guildMusicManager.getTrackScheduler().clearQueue();
        guildMusicManager.getTrackScheduler().stopCurrentSong();
        setIsPlaying(false);
    }
    public void skip(Guild guild){
        GuildMusicManager guildMusicManager = getGuildMusicManager(guild);
        guildMusicManager.getTrackScheduler().stopCurrentSong();
    }

    public void play(Guild guild, String trackURL) {
        GuildMusicManager guildMusicManager = getGuildMusicManager(guild);

        audioPlayerManager.loadItemOrdered(guildMusicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                guildMusicManager.getTrackScheduler().queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                guildMusicManager.getTrackScheduler().queue(playlist.getTracks().get(0));
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                guildMusicManager.getTrackScheduler().getEndBehavior();
            }
        });
    }

    public static String formatDurationToMMSS(long durationInMillis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(durationInMillis),
                TimeUnit.MILLISECONDS.toSeconds(durationInMillis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(durationInMillis))
        );
    }


    public void play(Guild guild, String trackURL, AudioBehavior audioBehavior){
        GuildMusicManager guildMusicManager = getGuildMusicManager(guild, audioBehavior);

        audioPlayerManager.loadItemOrdered(guildMusicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                audioBehavior.onLoadBehavior(track);
                guildMusicManager.getTrackScheduler().queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                guildMusicManager.getTrackScheduler().queue(playlist.getTracks().get(0));
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                guildMusicManager.getTrackScheduler().getEndBehavior();
                exception.printStackTrace();
            }
        });
    }

    public void play(Guild guild, String trackURL, AudioBehavior audioBehavior, MessageChannelUnion channel){
        GuildMusicManager guildMusicManager = getGuildMusicManager(guild, audioBehavior);

        audioPlayerManager.loadItemOrdered(guildMusicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                audioBehavior.onLoadBehavior(track);
                guildMusicManager.getTrackScheduler().queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                guildMusicManager.getTrackScheduler().queue(playlist.getTracks().get(0));
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                if(guildMusicManager.getTrackScheduler().getQueue().size() <= 1)
                    guildMusicManager.getTrackScheduler().getEndBehavior();
                channel.sendMessage("**❌ LavaPlayer Encountered an Error: " + exception.getMessage() + "**").complete();
                exception.printStackTrace();
            }
        });
    }

}