package Commands.Runnable;

import Commands.CommandBehavior;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.self.GenericSelfUpdateEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;

import java.util.Arrays;

public class Echo extends CommandBehavior {


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
       event.getChannel().retrieveMessageById(messageID).queue((msg) -> {
           String message = msg.getContentRaw();
           String[] message_split =  message.split( " ");
           if(message_split.length <= 1) return;
           message_split = Arrays.copyOfRange(message_split, 1, message_split.length);
           String parsed = "";
           for(String x : message_split) {
               parsed += x + " ";
           }
           event.getChannel().sendMessage(parsed).queue();
       });
    }

    @Override
    public void action(GenericRoleEvent event) {

    }
}
