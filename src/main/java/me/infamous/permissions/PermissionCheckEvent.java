package me.infamous.permissions;

import net.luckperms.api.util.Tristate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.eventbus.api.Event;

public class PermissionCheckEvent extends Event {

    private final CommandSourceStack source;
    private final String permission;
    private Tristate state = Tristate.UNDEFINED;

    public PermissionCheckEvent(CommandSourceStack source, String permission){
        this.source = source;
        this.permission = permission;
    }

    public Tristate getState(){
        return this.state;
    }

    public void setState(Tristate state){
        this.state = state;
    }

    public CommandSourceStack getSource() {
        return this.source;
    }

    public String getPermission() {
        return this.permission;
    }
}
