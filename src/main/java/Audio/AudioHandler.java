package Audio;

import net.dv8tion.jda.api.audio.AudioSendHandler;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class AudioHandler implements AudioSendHandler {
    private AudioInputStream audioStream;
    private byte[] buf;

    public AudioHandler(AudioInputStream audioStream) {
        this.audioStream = audioStream;
        this.buf = new byte[960 * audioStream.getFormat().getFrameSize() * audioStream.getFormat().getChannels()];
    }

    @Override
    public boolean canProvide() {
        try {
            return audioStream.available() > 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        try {
            int bytesRead = audioStream.read(this.buf, 0, this.buf.length);
            System.out.println("Sent " + bytesRead + " bytes of audio data");
            return ByteBuffer.wrap(this.buf, 0, bytesRead);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}