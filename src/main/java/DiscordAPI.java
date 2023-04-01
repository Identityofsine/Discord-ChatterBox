import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordAPI {
    private final String TOKEN = "MTA5MTUwNzQwNTI0MzgxODA2NA.GlE4wv.nOTc6FOaUV-fxvkPTRw_Vc1O1AqyZ-ItJQO7kQ";
    private static JDA jda;
    private static DiscordAPI instance; //singleton

    private DiscordAPI(){
        //init
        var builder = JDABuilder.createDefault(TOKEN);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.setActivity(Activity.playing("Horny"));
        jda = builder.build();
    }

    public static DiscordAPI getInstance(){
        if(instance == null) {
            DiscordAPI.instance = new DiscordAPI();
        }
        return instance;
    }

    public void addListener(ListenerAdapter event){
        jda.addEventListener(event);
    }
}

