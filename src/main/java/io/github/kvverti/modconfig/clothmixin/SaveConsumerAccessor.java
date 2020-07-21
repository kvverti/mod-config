package io.github.kvverti.modconfig.clothmixin;

import java.util.function.Consumer;

import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BooleanListEntry.class, remap = false)
public interface SaveConsumerAccessor {

    @Accessor
    Consumer<Boolean> getSaveConsumer();
}
