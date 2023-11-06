package me.infamous.permissions;

import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;
import java.util.Optional;

public class OptionRequestEvent extends Event {

    private final CommandSourceStack source;
    private final String key;
    @Nullable
    private String value;

    public OptionRequestEvent(CommandSourceStack source, String key){
        this.source = source;
        this.key = key;
    }

    public CommandSourceStack getSource() {
        return this.source;
    }

    public String getKey() {
        return this.key;
    }

    public Optional<String> getValue() {
        return Optional.ofNullable(this.value);
    }

    public void setValue(@Nullable String value) {
        this.value = value;
    }
}
