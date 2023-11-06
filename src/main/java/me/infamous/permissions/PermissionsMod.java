package me.infamous.permissions;

import com.mojang.logging.LogUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Mod(PermissionsMod.MODID)
public class PermissionsMod {
    public static final String MODID = "permissions_api";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final Set<UUID> MISSING_CAPS = new HashSet<>();

    public PermissionsMod() {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::onPermissionCheck);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::onOfflinePermissionCheck);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::onOptionRequest);
    }

    private void onPermissionCheck(PermissionCheckEvent event) {
        CommandSourceStack source = event.getSource();
        if(source.isPlayer()){
            ServerPlayer player = source.getPlayer();
            UUID uuid = player.getUUID();
            try{
                PermissionsMod.getPerms()
                        .map(lp -> lp.getPlayerAdapter(ServerPlayer.class).getUser(player))
                        .map(user -> user.getCachedData().getPermissionData().checkPermission(event.getPermission()))
                        .ifPresent(event::setState);
            } catch (IllegalStateException e){
                PermissionsMod.trackAndLogMissingCapability(uuid);
            }
        }
    }

    private void onOfflinePermissionCheck(OfflinePermissionCheckEvent event){
        try{
            PermissionsMod.getPerms()
                    .map(lp -> lp.getUserManager().loadUser(event.getUUID()))
                    .map(userFuture -> userFuture.thenApplyAsync(user -> user.getCachedData().getPermissionData().checkPermission(event.getPermission())))
                    .ifPresent(event::setState);
        } catch (IllegalStateException e){
            PermissionsMod.trackAndLogMissingCapability(event.getUUID());
        }
    }

    private void onOptionRequest(OptionRequestEvent event) {
        CommandSourceStack source = event.getSource();
        if(source.isPlayer()){
            ServerPlayer player = source.getPlayer();
            try{
                PermissionsMod.getPerms()
                        .map(lp -> lp.getPlayerAdapter(ServerPlayer.class).getUser(player))
                        .map(user -> user.getCachedData().getMetaData().getMetaValue(event.getKey()))
                        .ifPresent(event::setValue);
            } catch (IllegalStateException e){
                PermissionsMod.trackAndLogMissingCapability(player.getUUID());
            }
        }
    }

    public static Optional<LuckPerms> getPerms(){
        return Optional.ofNullable(FMLEnvironment.dist.isClient() ? null : LuckPermsProvider.get());
    }

    public static void trackAndLogMissingCapability(UUID uuid) {
        if(!MISSING_CAPS.contains(uuid)){
            MISSING_CAPS.add(uuid);
            LOGGER.error("LuckPerms UserCapability missing for {}", uuid);
        }
    }
}
