package io.github.kvverti.modconfig.clothmixin;

import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DropdownBoxEntry.SelectionElement.class)
public interface SelectionElementAccessor<R> {

    @Accessor(remap = false)
    DropdownBoxEntry.DropdownMenuElement<R> getMenu();
}
