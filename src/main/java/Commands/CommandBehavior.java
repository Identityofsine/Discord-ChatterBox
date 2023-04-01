package Commands;

import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.self.GenericSelfUpdateEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;

public interface CommandBehavior {
    void action(GenericEvent event);
    void action(GenericSelfUpdateEvent<?> event);

    void action(GenericUserEvent event);
    void action(GenericMessageEvent event);

    void action(GenericRoleEvent event);

}
