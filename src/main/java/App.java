import Commands.Command;
import Commands.CommandBehavior;
import Commands.CommandHandler;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.self.GenericSelfUpdateEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;

public class App {

    public static void main(String[] args) {
        DiscordAPI discord = DiscordAPI.getInstance();
        discord.addListener(new CommandHandler());
        CommandHandler.addCommands(new Command("java",
                new CommandBehavior() {
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
                }));
    }
}
