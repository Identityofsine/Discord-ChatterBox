package Audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class AudioCenter {

    private static AudioCenter instance;

    private AudioCenter(){

    }

    private static int calcBytesPerMS(int sample_rate, int bit_depth, int channels){
        return (sample_rate * bit_depth * channels) / 8000;
    }

    public static AudioCenter getInstance(){
        if(instance == null){
            instance = new AudioCenter();
        }
        return instance;
    }


    public AudioSendHandler createLavaPlayer(AudioPlayer adp){

        return new AudioSendHandler() {
            private final AudioPlayer audioPlayer = adp;
            private AudioFrame lastFrame;

            @Override
            public boolean canProvide() {
                lastFrame = audioPlayer.provide();
                return lastFrame != null;
            }

            @Override
            public ByteBuffer provide20MsAudio() {
                return ByteBuffer.wrap(lastFrame.getData());
            }
        };

    }

    public AudioSendHandler wavToAudioSendHandler(File file){
        AudioInputStream audioInputStream;
        AudioFormat targetFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                48000, // sample rate
                16, // sample size in bits
                2, // channels
                4, // frame size
                48000, // frame rate
                true); // big endian
        try{
            AudioInputStream _prev = AudioSystem.getAudioInputStream(file);
            AudioInputStream _conv = AudioSystem.getAudioInputStream(targetFormat, _prev);
            int numSilentBytes = (int) (targetFormat.getFrameRate() * 0.02) * targetFormat.getFrameSize();
            byte[] silentBytes = new byte[numSilentBytes];
            audioInputStream = _conv;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

        AudioSendHandler audioHandler = new AudioSendHandler() {
            private byte[] byteArray = new byte[20 * calcBytesPerMS(48000, 16, 2)];

            @Override
            public boolean canProvide() {
                try {
                    return audioInputStream.available() > 0;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            public ByteBuffer provide20MsAudio() {
                try {
                    int bytesRead = audioInputStream.read(byteArray, 0, byteArray.length);
                    if(bytesRead == -1) {
                        Thread.sleep(500);
                        audioInputStream.close();
                       return ByteBuffer.allocate(0);
                    };
                    return ByteBuffer.wrap(byteArray, 0, bytesRead);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public boolean isOpus(){
                return false;
            }
        };



        return audioHandler;
    }

}
