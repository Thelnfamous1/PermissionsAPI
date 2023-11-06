/*
 * This file is part of fabric-permissions-api, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package me.lucko.fabric.api.permissions.v0;

import com.mojang.authlib.GameProfile;
import me.infamous.permissions.OfflinePermissionCheckEvent;
import me.infamous.permissions.PermissionCheckEvent;
import net.luckperms.api.util.Tristate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * A simple permissions API.
 */
public interface Permissions {

    /**
     * Gets the {@link Tristate state} of a {@code permission} for the given source.
     *
     * @param source the source
     * @param permission the permission
     * @return the state of the permission
     */
    static @NotNull Tristate getPermissionValue(@NotNull CommandSourceStack source, @NotNull String permission) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(permission, "permission");
        PermissionCheckEvent event = new PermissionCheckEvent(source, permission);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getState();
    }

    /**
     * Performs a permission check, falling back to the {@code defaultValue} if the resultant
     * state is {@link Tristate#UNDEFINED}.
     *
     * @param source the source to perform the check for
     * @param permission the permission to check
     * @param defaultValue the default value to use if nothing has been set
     * @return the result of the permission check
     */
    static boolean check(@NotNull CommandSourceStack source, @NotNull String permission, boolean defaultValue) {
        Tristate permissionValue = getPermissionValue(source, permission);
        if(permissionValue == Tristate.UNDEFINED) return defaultValue;
        return permissionValue.asBoolean();
    }

    /**
     * Performs a permission check, falling back to requiring the {@code defaultRequiredLevel}
     * if the resultant state is {@link Tristate#UNDEFINED}.
     *
     * @param source the source to perform the check for
     * @param permission the permission to check
     * @param defaultRequiredLevel the required permission level to check for as a fallback
     * @return the result of the permission check
     */
    static boolean check(@NotNull CommandSourceStack source, @NotNull String permission, int defaultRequiredLevel) {
        Tristate permissionValue = getPermissionValue(source, permission);
        if(permissionValue == Tristate.UNDEFINED) return source.hasPermission(defaultRequiredLevel);
        return permissionValue.asBoolean();
    }

    /**
     * Performs a permission check, falling back to {@code false} if the resultant state
     * is {@link Tristate#UNDEFINED}.
     *
     * @param source the source to perform the check for
     * @param permission the permission to check
     * @return the result of the permission check
     */
    static boolean check(@NotNull CommandSourceStack source, @NotNull String permission) {
        return getPermissionValue(source, permission).asBoolean();
    }

    /**
     * Creates a predicate which returns the result of performing a permission check,
     * falling back to the {@code defaultValue} if the resultant state is {@link Tristate#UNDEFINED}.
     *
     * @param permission the permission to check
     * @param defaultValue the default value to use if nothing has been set
     * @return a predicate that will perform the permission check
     */
    static @NotNull Predicate<CommandSourceStack> require(@NotNull String permission, boolean defaultValue) {
        Objects.requireNonNull(permission, "permission");
        return player -> check(player, permission, defaultValue);
    }

    /**
     * Creates a predicate which returns the result of performing a permission check,
     * falling back to requiring the {@code defaultRequiredLevel} if the resultant state is
     * {@link Tristate#UNDEFINED}.
     *
     * @param permission the permission to check
     * @param defaultRequiredLevel the required permission level to check for as a fallback
     * @return a predicate that will perform the permission check
     */
    static @NotNull Predicate<CommandSourceStack> require(@NotNull String permission, int defaultRequiredLevel) {
        Objects.requireNonNull(permission, "permission");
        return player -> check(player, permission, defaultRequiredLevel);
    }

    /**
     * Creates a predicate which returns the result of performing a permission check,
     * falling back to {@code false} if the resultant state is {@link Tristate#UNDEFINED}.
     *
     * @param permission the permission to check
     * @return a predicate that will perform the permission check
     */
    static @NotNull Predicate<CommandSourceStack> require(@NotNull String permission) {
        Objects.requireNonNull(permission, "permission");
        return player -> check(player, permission);
    }

    /**
     * Gets the {@link Tristate state} of a {@code permission} for the given entity.
     *
     * @param entity the entity
     * @param permission the permission
     * @return the state of the permission
     */
    static @NotNull Tristate getPermissionValue(@NotNull Entity entity, @NotNull String permission) {
        Objects.requireNonNull(entity, "entity");
        return getPermissionValue(entity.createCommandSourceStack(), permission);
    }

    /**
     * Performs a permission check, falling back to the {@code defaultValue} if the resultant
     * state is {@link Tristate#UNDEFINED}.
     *
     * @param entity the entity to perform the check for
     * @param permission the permission to check
     * @param defaultValue the default value to use if nothing has been set
     * @return the result of the permission check
     */
    static boolean check(@NotNull Entity entity, @NotNull String permission, boolean defaultValue) {
        Objects.requireNonNull(entity, "entity");
        return check(entity.createCommandSourceStack(), permission, defaultValue);
    }

    /**
     * Performs a permission check, falling back to requiring the {@code defaultRequiredLevel}
     * if the resultant state is {@link Tristate#UNDEFINED}.
     *
     * @param entity the entity to perform the check for
     * @param permission the permission to check
     * @param defaultRequiredLevel the required permission level to check for as a fallback
     * @return the result of the permission check
     */
    static boolean check(@NotNull Entity entity, @NotNull String permission, int defaultRequiredLevel) {
        Objects.requireNonNull(entity, "entity");
        return check(entity.createCommandSourceStack(), permission, defaultRequiredLevel);
    }

    /**
     * Performs a permission check, falling back to {@code false} if the resultant state
     * is {@link Tristate#UNDEFINED}.
     *
     * @param entity the entity to perform the check for
     * @param permission the permission to check
     * @return the result of the permission check
     */
    static boolean check(@NotNull Entity entity, @NotNull String permission) {
        Objects.requireNonNull(entity, "entity");
        return check(entity.createCommandSourceStack(), permission);
    }

    /**
     * Gets the {@link Tristate state} of a {@code permission} for the given (potentially) offline player.
     *
     * @param uuid the player uuid
     * @param permission the permission
     * @return the state of the permission
     */
    static @NotNull CompletableFuture<Tristate> getPermissionValue(@NotNull UUID uuid, @NotNull String permission) {
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(permission, "permission");
        OfflinePermissionCheckEvent event = new OfflinePermissionCheckEvent(uuid, permission);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getState();
    }

    /**
     * Performs a permission check, falling back to the {@code defaultValue} if the resultant
     * state is {@link Tristate#UNDEFINED}.
     *
     * @param uuid the player to perform the check for
     * @param permission the permission to check
     * @param defaultValue the default value to use if nothing has been set
     * @return the result of the permission check
     */
    static CompletableFuture<Boolean> check(@NotNull UUID uuid, @NotNull String permission, boolean defaultValue) {
        CompletableFuture<Tristate> permissionValue = getPermissionValue(uuid, permission);
        return permissionValue.thenApplyAsync(state -> {
            if(state == Tristate.UNDEFINED) return defaultValue;
            return state.asBoolean();
        });
    }

    /**
     * Performs a permission check, falling back to {@code false} if the resultant state
     * is {@link Tristate#UNDEFINED}.
     *
     * @param uuid the source to perform the check for
     * @param permission the permission to check
     * @return the result of the permission check
     */
    static CompletableFuture<Boolean> check(@NotNull UUID uuid, @NotNull String permission) {
        return getPermissionValue(uuid, permission).thenApplyAsync(Tristate::asBoolean);
    }

    /**
     * Performs a permission check, falling back to {@code false} if the resultant state
     * is {@link Tristate#UNDEFINED}.
     *
     * @param profile the player profile to perform the check for
     * @param permission the permission to check
     * @param defaultValue the default value to use if nothing has been set
     * @return the result of the permission check
     */
    static CompletableFuture<Boolean> check(@NotNull GameProfile profile, @NotNull String permission, boolean defaultValue) {
        Objects.requireNonNull(profile, "profile");
        return check(profile.getId(), permission, defaultValue);
    }

    /**
     * Performs a permission check, falling back to {@code false} if the resultant state
     * is {@link Tristate#UNDEFINED}.
     *
     * @param profile the player profile to perform the check for
     * @param permission the permission to check
     * @return the result of the permission check
     */
    static CompletableFuture<Boolean> check(@NotNull GameProfile profile, @NotNull String permission) {
        Objects.requireNonNull(profile, "profile");
        return check(profile.getId(), permission);
    }

    /**
     * Performs a permission check, falling back to requiring the {@code defaultRequiredLevel}
     * if the resultant state is {@link Tristate#UNDEFINED}.
     *
     * @param profile the player profile to perform the check for
     * @param permission the permission to check
     * @param defaultRequiredLevel the required permission level to check for as a fallback
     * @param server instance to check permission level
     * @return the result of the permission check
     */
    static CompletableFuture<Boolean> check(@NotNull GameProfile profile, @NotNull String permission, int defaultRequiredLevel, @NotNull MinecraftServer server) {
        Objects.requireNonNull(profile, "profile");
        Objects.requireNonNull(server, "server");
        BooleanSupplier permissionLevelCheck = () -> server.getProfilePermissions(profile) >= defaultRequiredLevel;
        return getPermissionValue(profile.getId(), permission).thenApplyAsync(state -> {
            if(state == Tristate.UNDEFINED) return permissionLevelCheck.getAsBoolean();
            return state.asBoolean();
        });
    }

}
