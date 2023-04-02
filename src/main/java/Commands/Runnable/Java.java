package Commands.Runnable;

import Commands.Command;
import Commands.CommandBehavior;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.self.GenericSelfUpdateEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;

public class Java extends CommandBehavior {
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
            event.getChannel().sendMessage("Hello World from the JVM... P.S Fuck You <@355214795957338115>").queue();
        }

        @Override
        public void action(GenericRoleEvent event) {

        }
}
