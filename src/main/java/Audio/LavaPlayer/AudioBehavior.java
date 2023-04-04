package Audio.LavaPlayer;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public interface AudioBehavior {
    void queueBehavior(AudioTrack track);

    void onLoadBehavior(AudioTrack track);

    void endBehavior(AudioTrack track);
}
