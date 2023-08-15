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

import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Simple permissions check event for (potentially) offline players.
 */

@Event.HasResult
public class OfflinePermissionCheckEvent extends Event {

    private final UUID uuid;
    private final String permission;

    public OfflinePermissionCheckEvent(@NotNull UUID uuid, @NotNull String permission){

        this.uuid = uuid;
        this.permission = permission;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPermission() {
        return permission;
    }

    /*
    Event<OfflinePermissionCheckEvent> EVENT = EventFactory.createArrayBacked(OfflinePermissionCheckEvent.class, (callbacks) -> (uuid, permission) -> {
        CompletableFuture<TriState> res = CompletableFuture.completedFuture(TriState.DEFAULT);
        for (OfflinePermissionCheckEvent callback : callbacks) {
            res = res.thenCompose(triState -> {
                if (triState != TriState.DEFAULT) {
                    return CompletableFuture.completedFuture(triState);
                }
                return callback.onPermissionCheck(uuid, permission);
            });
        }
        return res;
    });

    @NotNull CompletableFuture<TriState> onPermissionCheck(@NotNull UUID uuid, @NotNull String permission);
     */

}
