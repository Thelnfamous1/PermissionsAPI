package me.infamous.permissions;

import net.luckperms.api.util.Tristate;
import net.minecraftforge.eventbus.api.Event;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class OfflinePermissionCheckEvent extends Event {

    private final UUID source;
    private final String permission;
    private CompletableFuture<Tristate> state = CompletableFuture.completedFuture(Tristate.UNDEFINED);

    public OfflinePermissionCheckEvent(UUID source, String permission){
        this.source = source;
        this.permission = permission;
    }

    public CompletableFuture<Tristate> getState(){
        return this.state;
    }

    public void setState(CompletableFuture<Tristate> state){
        this.state = this.state.thenCompose(triState -> {
            if (triState != Tristate.UNDEFINED) {
                return CompletableFuture.completedFuture(triState);
            }
            return state;
        });
    }

    public UUID getUUID() {
        return this.source;
    }

    public String getPermission() {
        return this.permission;
    }
}
