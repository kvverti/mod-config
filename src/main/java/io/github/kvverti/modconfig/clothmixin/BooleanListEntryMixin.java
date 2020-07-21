package io.github.kvverti.modconfig.clothmixin;

import java.util.function.Consumer;

import io.github.kvverti.modconfig.data.facade.BooleanFacade;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.text.Text;

@Mixin(value = BooleanListEntry.class, remap = false)
public abstract class BooleanListEntryMixin extends TooltipListEntry<Boolean> implements BooleanFacade {

    @Shadow
    @Final
    private Consumer<Boolean> saveConsumer;

    private BooleanListEntryMixin() {
        super(null, null);
    }

    @Override
    public Text modcfg_getOptionName() {
        return this.getFieldName();
    }

    @Override
    public Boolean modcfg_getValue() {
        return this.getValue();
    }

    @Override
    public Consumer<Boolean> modcfg_getSaveHandler() {
        return this.saveConsumer;
    }
}
