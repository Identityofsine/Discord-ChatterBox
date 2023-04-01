package Commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.TreeMap;

public class CommandHandler extends ListenerAdapter {

    private static TreeMap<String, Command> commands = new TreeMap<String,Command>();

    private static boolean isValidCommand(String command) {
        try{
            return command.charAt(0) == '(' && command.charAt(1) == ')';
        } catch(Exception e) {
            return false;
        }

    }

    private static Command findCommand(String name){
        String command = name.substring(2, name.length());
        return commands.get(command);
    }

    public static boolean addCommands(Command cmd){
        return commands.putIfAbsent(cmd.commandName,cmd) == null;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        String message = event.getMessage().getContentStripped();
        if(message.length() < 3) return;
        String[] split = message.split(" ");
        String command = split[0];
        if(isValidCommand(command)){
            System.out.println(message);
            Command cmd = findCommand(command);
            if(cmd != null){
                event.getMessage().getChannel().sendMessage(String.format("COMMAND HANDLER WORKING @ You Sent a valid command : %s", cmd.commandName)).queue();
                cmd.behavior.action(event);
            }
            else return;
        }




    }
}
