package Audio.LavaPlayer;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public interface AudioQueueBehavior {
    void fire(AudioTrack track);
}
