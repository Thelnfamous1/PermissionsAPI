package me.infamous.permissions;

import com.mojang.logging.LogUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
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

    public PermissionsMod() {}

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
