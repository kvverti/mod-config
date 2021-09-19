package io.github.kvverti.modconfig.clothmixin;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import io.github.kvverti.modconfig.data.facade.CycleOptionFacade;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.text.Text;

@Mixin(SelectionListEntry.class)
public abstract class SelectionListEntryMixin<T> extends TooltipListEntry<T> implements CycleOptionFacade<T> {

    @Final
    @Shadow(remap = false)
    private ImmutableList<T> values;

    @Final
    @Shadow(remap = false)
    private Function<T, Text> nameProvider;

    @Final
    @Shadow(remap = false)
    private Consumer<T> saveConsumer;

    private SelectionListEntryMixin() {
        super(null, null);
    }

    @Override
    public List<T> modcfg_getStates() {
        return this.values;
    }

    @Override
    public Text modcfg_getOptionName() {
        return this.getFieldName();
    }

    @Override
    public T modcfg_getValue() {
        return this.getValue();
    }

    @Override
    public Function<T, Text> modcfg_getNameProvider() {
        return nameProvider;
    }

    @Override
    public Consumer<T> modcfg_getSaveHandler() {
        return this.saveConsumer;
    }
}
