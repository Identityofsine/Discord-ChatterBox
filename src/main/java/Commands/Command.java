package Commands;

import Commands.Arguments.Argument;
import Commands.Arguments.Arguments;

public class Command {
    public String commandName = "";
    public CommandBehavior behavior;

    public Arguments arguments;

    private boolean requireArguments;

    public Command(String name, CommandBehavior behavior){
        this.commandName = name;
        this.behavior = behavior;
        arguments = new Arguments();
        requireArguments = false;
    }
    public Command(String name, CommandBehavior behavior, boolean arguments){
        this(name, behavior);
        requireArguments = true;
    }






    @Override
    public boolean equals(Object o){
        if(o instanceof Command) {
            return ((Command)o).commandName.equals(this.commandName);
        }
        return false;
    }

}
