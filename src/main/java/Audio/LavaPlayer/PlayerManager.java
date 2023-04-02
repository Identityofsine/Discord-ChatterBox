package Audio.LavaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

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

    public GuildMusicManager getGuildMusicManager(Guild guild) {
        return guildMusicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager musicManager = new GuildMusicManager(audioPlayerManager, guild, () -> {
                guild.getAudioManager().closeAudioConnection();
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
    public void play(Guild guild, String trackURL, AudioQueueBehavior queueBehavior){
        this.play(guild,trackURL);
        getGuildMusicManager(guild).getTrackScheduler().setQueueBehavior(queueBehavior);
    }
}