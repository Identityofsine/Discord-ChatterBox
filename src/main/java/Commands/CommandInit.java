package Commands;

import Commands.Runnable.*;

public class CommandInit {

    public static void addChatCommands(){
        CommandHandler.addCommands(new Command("java", new Java()));
        CommandHandler.addCommands(new Command("echo", new Echo()));
        CommandHandler.addCommands(new Command("join", new Join()));
        CommandHandler.addCommands(new Command("stop", new Stop()));
        CommandHandler.addCommands(new Command("skip", new Skip()));
        CommandHandler.addCommands(new Command("play", new Play()));
        CommandHandler.addCommands(new Command("queue", new Queue()));

    }

}
