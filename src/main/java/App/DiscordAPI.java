package App;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.EnumSet;

public class DiscordAPI {
    private final String TOKEN = Dotenv.load().get("API_KEY");

    private static JDA jda;
    private static DiscordAPI instance; //singleton

    private DiscordAPI(){
        //init
        var builder = JDABuilder.createDefault(TOKEN);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_VOICE_STATES);
        builder.disableCache(EnumSet.of(
                CacheFlag.CLIENT_STATUS,
                CacheFlag.ACTIVITY,
                CacheFlag.EMOJI
        ));
        builder.enableCache(CacheFlag.VOICE_STATE);
        builder.setActivity(Activity.playing("Nick's Sex Tips"));
        jda = builder.build();
    }

    public static DiscordAPI getInstance(){
        if(instance == null) {
            DiscordAPI.instance = new DiscordAPI();
        }
        return instance;
    }

    public JDA getJda(){
        return jda;
    }

    public void addListener(ListenerAdapter event){
        jda.addEventListener(event);
    }
}

