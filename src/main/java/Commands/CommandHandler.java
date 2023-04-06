package Commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.TreeMap;

public class CommandHandler extends ListenerAdapter {

    public static Object CommandLock = new Object();
    private static TreeMap<String, Command> commands = new TreeMap<String,Command>();
    private static final char FIRSTKEY = '!';
    private static final char SECONDKEY = '^';

    private static boolean isValidCommand(String command) {
        try{
            return command.charAt(0) == FIRSTKEY && command.charAt(1) == SECONDKEY;
        } catch(Exception e) {
            return false;
        }

    }

    private static Command findCommand(String name){
        synchronized (CommandLock){
            String command = name.substring(2, name.length());
            return commands.get(command);
        }
    }

    public static boolean removeCommands(Command... cmds){
        synchronized (CommandLock){
            for(int i = 0; i <= cmds.length; i++){
                commands.remove(cmds[i].commandName);
            }
        }
    }

    public static boolean addCommands(Command cmd){
        synchronized (CommandLock){
            return commands.putIfAbsent(cmd.commandName,cmd) == null;
        }
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        String message = event.getMessage().getContentStripped();
        if(event.getAuthor().isBot()) return;
        if(message.length() < 3) return;
        String[] split = message.split(" ");
        String command = split[0];
        if(isValidCommand(command)){
            System.out.println(message);
            Command cmd = findCommand(command);
            if(cmd != null){
                System.out.println(String.format("COMMAND HANDLER WORKING @ You Sent a valid command : %s", cmd.commandName));
                cmd.behavior.action(event);
            }
            else return;
        }




    }
}
