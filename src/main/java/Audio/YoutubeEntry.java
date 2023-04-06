package Audio;

public class YoutubeEntry {

    private String ID;
    private String name;
    private String channel;

    public YoutubeEntry(String id, String name, String channel){
        this.ID = id;
        this.name = name;
        this.channel = channel;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getChannel() {
        return channel;
    }
}
