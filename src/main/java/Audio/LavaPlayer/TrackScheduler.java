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

    public static final Object lock = new Object();
    private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
    private AudioTrack currentTrack = null;
    private boolean isRepeat = false;

    private final AudioBehavior audioBehavior;


    public TrackScheduler(AudioPlayer player, AudioBehavior audioBehavior) {
        this.player = player;
        this.audioBehavior = audioBehavior;
    }

    public void clearQueue(){
        queue.clear();
    }

    public void stopCurrentSong(){
        player.stopTrack();
    }


    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(isRepeat) {
            player.startTrack(track.makeClone(), false);
        } else {
            PlayerManager.get().setIsPlaying(queue.isEmpty());
            synchronized (lock){
                if(queue.isEmpty())
                    audioBehavior.endBehavior(null);
                else
                    audioBehavior.queueBehavior(queue.peek());
                player.startTrack(queue.poll(), false);
                lock.notify();
            }


        }
    }

    public void queue(AudioTrack track) {
        synchronized (lock){
            if(!player.startTrack(track, true)) {
                queue.offer(track);
            } else{
                currentTrack = track;
                PlayerManager.get().setIsPlaying(true);
            }
            lock.notify();
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
            audioBehavior.endBehavior(null);
        return queue.isEmpty();
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }
}