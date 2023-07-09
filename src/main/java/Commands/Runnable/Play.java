package Commands.Runnable;

import API.Parameter;
import API.Requester;
import App.Util;
import Audio.LavaPlayer.AudioBehavior;
import Audio.LavaPlayer.PlayerManager;
import Audio.YoutubeEntry;
import Commands.Arguments.Argument;
import Commands.CommandBehavior;
import com.fasterxml.jackson.databind.JsonNode;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.self.GenericSelfUpdateEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

class YoutubeInfo{

}


public class Play extends CommandBehavior {

    //if yes, listen for !^play 1, !^play 2, !^play 3; etc
    private static boolean hasYoutubeCommandRan = false;

    //delete after use
    private static LinkedList<YoutubeEntry> ytVideos;

    //will return null if not a url
    private YoutubeInfo grabYoutubeStats(String url){
        return null;
    }

    public Play(){
        this.addRole("DJ", "MUSIC");
        this.addArgument(new Argument<String>("url", "Link"));
    }

    protected boolean isLink(String urlString) {
        try {
            URL url = new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private void handleSearchLogic(String query, GenericMessageEvent event) {
        Requester ytSearch = new Requester(Dotenv.load().get("YT_API_KEY"), "https://youtube.googleapis.com/youtube/v3/search");
        try{
            JsonNode results = ytSearch.GetRequest(new Parameter("q", query), new Parameter("part", "snippet"));
            JsonNode items = results.get("items");
//            System.out.println("ITEMS : " + items.toPrettyString());
            ytVideos = new LinkedList<YoutubeEntry>();
            StringBuilder message_precursor = new StringBuilder();
            for(int i = 0; i < items.size(); i++) {
                JsonNode found_item = items.get(i);
//                System.out.println("FOUND_ITEM : " + found_item.toPrettyString());
                if(!found_item.get("id").get("kind").asText().equals("youtube#video"))
                    continue;
                JsonNode snippet = found_item.get("snippet"); //Corresponds with the JSON file
                ytVideos.add(new YoutubeEntry(found_item.get("id").get("videoId").asText(), snippet.get("title").asText(), snippet.get("channelTitle").asText()));
            }
            message_precursor.append("**Found Videos for ").append(query.replace("%20", " ")).append("**\n");
            message_precursor.append(Util.repeatString("#=",message_precursor.toString().length())).append("\n");
            int index = 1;
            for(YoutubeEntry video : ytVideos){
                message_precursor.append(String.format("***%d : %s by %s***", index++, video.getName(),video.getChannel())).append("\n");
            }
            message_precursor.append("\nType !^play (1-4) to play");
            event.getChannel().sendMessage(message_precursor.toString()).complete();
            hasYoutubeCommandRan = true;
            //run hooks

        }
        catch (Exception e){
            event.getChannel().sendMessage("**Sorry... something went wrong with fulfilling your request...**").complete();
            e.printStackTrace();
        }
    }

    @Override
    protected void action(GenericEvent event) {

    }

    @Override
    public void action(GenericSelfUpdateEvent<?> event) {

    }

    @Override
    public void action(GenericUserEvent event) {

    }

    @Override
    public void action(GenericMessageEvent event) {
        String messageID = event.getMessageId();
        Message msg = event.getChannel().retrieveMessageById(messageID).complete();
        String msg_String = msg.getContentRaw();
        String[] arr = msg_String.split(" ");
        if(arr.length <= 1) {
            event.getChannel().sendMessage("Missing Youtube URL").queue();
            return;
        }

        if(msg.getMember() == null) return;

        if(!isUserAllowed(msg.getMember())) {
            event.getChannel().sendMessage("**You are not allowed to use this command...**").queue();
            return;
        }

        String arg = "";
        //argument finder
        if(arr.length > 2){
            for(String word : arr){
                if(word.length() < 3) continue;
                if(word.charAt(0) == '#' && word.charAt(1) == '$'){
                    arg = word;
                    break;
                }
            }
            System.out.println("Argument Found : " + arg);
        }
        String url = "";
        try {
            url = arr[1];
        }
        catch(ArrayIndexOutOfBoundsException e){
            event.getChannel().sendMessage("Missing Youtube URL").queue();
            return;
        }
        if((!isLink(url) || arr.length > 2) && !hasYoutubeCommandRan){
            StringBuilder oldString = new StringBuilder();
            for(int i = 1; i < arr.length; i++){
                if(arr[i].equals(arg)) continue;
                oldString.append(arr[i]).append(i == arr.length - 1 ? "" : "%20");
            }

            this.handleSearchLogic(oldString.toString(), event);
            return;
        }

        if(hasYoutubeCommandRan && !isLink(url)){
            try{
                int index = Integer.parseInt(url);
                if(index < 1 || index > 4){
                    event.getChannel().sendMessage("**Your query is out of range. *(1-4) only*").complete();
                    return;
                }
                this.getArgument("url").setValue(String.format("https://www.youtube.com/watch?v=%s", ytVideos.get(index - 1).getID()));
                hasYoutubeCommandRan = false;
            }
            catch (NumberFormatException e) {
                event.getChannel().sendMessage("**Researching...**").complete();
                hasYoutubeCommandRan = false;
                action(event);
                return;
            }
            catch (IndexOutOfBoundsException e) {
                event.getChannel().sendMessage("**Somehow you managed to break the !^play (1-4) function, I don't know how but I'm missing that video in memory; sorry about that...**").complete();
                hasYoutubeCommandRan = false;
                return;
            }
        } else{
            if(hasYoutubeCommandRan)
                event.getChannel().sendMessage("*Ignoring Youtube Search...*").complete();
            this.getArgument("url").setValue(url);
            hasYoutubeCommandRan = false;
        }
        Member user = CommandBehavior.getMessageSender(event);
        try{
            VoiceChannel voiceChannel = user.getVoiceState().getChannel().asVoiceChannel();

            Guild guild = voiceChannel.getGuild();


            PlayerManager audioHandler = PlayerManager.get();
            guild.getAudioManager().openAudioConnection(voiceChannel);
            audioHandler.play(event.getGuild(), (String) this.getArgument("url").getValue(), new AudioBehavior() {
                @Override
                public void queueBehavior(AudioTrack track) {
                    event.getChannel().sendMessage(MarkdownUtil.bold(String.format("Now Playing : *%s* (%s)", track.getInfo().title, PlayerManager.formatDurationToMMSS(track.getDuration())))).queue();
                }

                @Override
                public void onLoadBehavior(AudioTrack track) {
                    if(!audioHandler.isPlaying())
                        event.getChannel().sendMessage(MarkdownUtil.bold(String.format("Now Playing : *%s* (%s)", track.getInfo().title, PlayerManager.formatDurationToMMSS(track.getDuration())))).queue();
                    else
                        event.getChannel().sendMessage(MarkdownUtil.bold(String.format("Added \"*%s*\" (%s) to queue!", track.getInfo().title, PlayerManager.formatDurationToMMSS(track.getDuration())))).queue();
                }

                @Override
                public void endBehavior(AudioTrack track) {
                    guild.getAudioManager().closeAudioConnection();
                }
            }, event.getChannel());
        }
        catch (NullPointerException e){
            event.getChannel().sendMessage("You aren't in a voice channel! **(or I can't join...)**").complete();
        }
        catch (FriendlyException e) {
            event.getChannel().sendMessage("**❌ LavaPlayer Encountered an Error: " + e.getMessage() + "**").complete();
        }
    }

    @Override
    public void action(GenericRoleEvent event) {

    }
}
