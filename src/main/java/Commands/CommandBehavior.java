package Commands;

import Commands.Arguments.Argument;
import Commands.Arguments.Arguments;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.self.GenericSelfUpdateEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;
import net.dv8tion.jda.internal.requests.Route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class CommandBehavior {

    public Arguments arguments;

    public List<Commands.Permission.Role> allowed_roles;



    protected CommandBehavior(){
        this.arguments = new Arguments<String>();
        this.allowed_roles = new ArrayList<>();
    }

    protected abstract void action(GenericEvent event);

    public abstract void action(GenericSelfUpdateEvent<?> event);

    public abstract void action(GenericUserEvent event);

    public abstract void action(GenericMessageEvent event);

    public abstract void action(GenericRoleEvent event);

    protected void addArgument(Argument...args){
        for (Argument arg : args ) {
            arguments.addArgs(arg);
        }
    }

    protected void addRole(String... roles){
        for (String role_name : roles ) {
            allowed_roles.add(new Commands.Permission.Role(role_name));
        }
    }

    protected <E> Argument<E> getArgument(int index){
        return (Argument<E>) arguments.args.get(index);
    }

    protected <E> Argument<E> getArgument(String text){
        Argument<E> object = null;
        Iterator iterator = arguments.args.iterator();
        while(iterator.hasNext()){
            Object i = iterator.next();
            if(((Argument<E>)i).argName.equals(text)){
                object = (Argument<E>)i;
            }
        }
        return object;
    }

    private boolean roleExists(Role role_object) {
        for(Commands.Permission.Role role : allowed_roles){
            System.out.println("[DEBUG] : " + role.getRoleName() + " : " + role_object.toString());
            if(role.equals(role_object)){
                return true;
            }
        }
        return false;
    }

    protected boolean isUserAllowed(Member member) {
        List<Role> user_roles = member.getRoles();
        for(Role server_role : user_roles){
            if(this.roleExists(server_role)){
                return true;
            }
        }
        return false;
    }

    public static Member getMessageSender(GenericMessageEvent msgevent){
        long msgID = msgevent.getMessageIdLong();
        return msgevent.getChannel().retrieveMessageById(msgID).complete().getMember();
    }

    static void sendMessage(GenericMessageEvent msgevent, String content){
        msgevent.getChannel().sendMessage(content).queue();
    }

}
