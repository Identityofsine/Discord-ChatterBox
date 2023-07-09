package Commands.Runnable;

import Audio.LavaPlayer.PlayerManager;
import Commands.CommandBehavior;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.self.GenericSelfUpdateEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;

public class Stop extends CommandBehavior {

    public Stop() {
        this.addRole("DJ", "MUSIC");
    }

    @Override
    public void action(GenericEvent event) {

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

        if(msg.getMember() == null) return;
        if(!this.isUserAllowed(msg.getMember())) {
            event.getChannel().sendMessage("**You are not allowed to use this command...**").queue();
            return;
        }

        PlayerManager.get().stop(event.getGuild());
        event.getGuild().getAudioManager().closeAudioConnection();
    }

    @Override
    public void action(GenericRoleEvent event) {

    }
}
