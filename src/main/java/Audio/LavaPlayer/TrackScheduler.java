package Audio.LavaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import javax.sound.midi.Track;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
    private AudioTrack currentTrack = null;
    private boolean isRepeat = false;

    private final AudioEndBehavior endBehavior;

    private AudioQueueBehavior queueBehavior;

    public TrackScheduler(AudioPlayer player, AudioEndBehavior endBehavior, AudioQueueBehavior queueBehavior) {
        this.player = player;
        this.endBehavior = endBehavior;
        this.queueBehavior = queueBehavior;
    }

    public void clearQueue(){
        queue.clear();
    }

    public void stopCurrentSong(){
        player.stopTrack();
    }

    public void setQueueBehavior(AudioQueueBehavior quebe){
        this.queueBehavior = quebe;
    }
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(isRepeat) {
            player.startTrack(track.makeClone(), false);
        } else {
            PlayerManager.get().setIsPlaying(queue.isEmpty());
            if(queue.isEmpty())
                endBehavior.end();
            else
                queueBehavior.fire(queue.peek());
            player.startTrack(queue.poll(), false);

        }
    }

    public void queue(AudioTrack track) {
        if(!player.startTrack(track, true)) {
            queue.offer(track);
        } else{
            currentTrack = track;
            PlayerManager.get().setIsPlaying(true);
        }
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public AudioTrack getCurrentTrack(){
        return currentTrack;
    }

    public boolean getEndBehavior(){
        if(queue.isEmpty())
            endBehavior.end();
        return queue.isEmpty();
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }
}