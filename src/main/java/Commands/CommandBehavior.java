package Commands;

import Commands.Arguments.Argument;
import Commands.Arguments.Arguments;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.self.GenericSelfUpdateEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;

public abstract class CommandBehavior {

    Arguments arguments;

    protected CommandBehavior(){
        this.arguments = new Arguments<String>();
    }

    protected abstract void action(GenericEvent event);

    public abstract void action(GenericSelfUpdateEvent<?> event);

    public abstract void action(GenericUserEvent event);

    public abstract void action(GenericMessageEvent event);

    public abstract void action(GenericRoleEvent event);

    protected void addArgument(Argument...args){
        for (var arg : args ) {
            arguments.addArgs(arg);
        }
    }

    protected <E> Argument<E> getArgument(int index){
        return (Argument<E>) arguments.args.get(index);
    }

    protected <E> Argument<E> getArgument(String text){
        Argument<E> object = null;
        var iterator = arguments.args.iterator();
        while(iterator.hasNext()){
            var i = iterator.next();
            if(((Argument<E>)i).argName.equals(text)){
                object = (Argument<E>)i;
            }
        }
        return object;
    }

    public static Member getMessageSender(GenericMessageEvent msgevent){
        long msgID = msgevent.getMessageIdLong();
        return msgevent.getChannel().retrieveMessageById(msgID).complete().getMember();
    }

    static void sendMessage(GenericMessageEvent msgevent, String content){
        msgevent.getChannel().sendMessage(content).queue();
    }

}
