package Commands;

public class Command {
    public String commandName = "";
    public CommandBehavior behavior;

    public Command(String name, CommandBehavior behavior){
        this.commandName = name;
        this.behavior = behavior;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Command) {
            return ((Command)o).commandName.equals(this.commandName);
        }
        return false;
    }

}
