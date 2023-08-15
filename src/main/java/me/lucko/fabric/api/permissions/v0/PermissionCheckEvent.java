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

import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Simple permissions check event for {@link SharedSuggestionProvider}s.
 */
@Event.HasResult
public class PermissionCheckEvent extends Event {

    private final SharedSuggestionProvider source;
    private final String permission;

    public PermissionCheckEvent(@NotNull SharedSuggestionProvider source, @NotNull String permission){

        this.source = source;
        this.permission = permission;
    }

    public SharedSuggestionProvider getSource() {
        return source;
    }

    public String getPermission() {
        return permission;
    }

    /*
    Event<PermissionCheckEvent> EVENT = EventFactory.createArrayBacked(PermissionCheckEvent.class, (callbacks) -> (source, permission) -> {
        for (PermissionCheckEvent callback : callbacks) {
            TriState state = callback.onPermissionCheck(source, permission);
            if (state != TriState.DEFAULT) {
                return state;
            }
        }
        return TriState.DEFAULT;
    });

    @NotNull TriState onPermissionCheck(@NotNull CommandSource source, @NotNull String permission);
     */

}
