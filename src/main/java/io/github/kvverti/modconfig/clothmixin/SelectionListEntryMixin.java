package io.github.kvverti.modconfig.clothmixin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import io.github.kvverti.modconfig.data.facade.CycleOptionFacade;
import io.github.kvverti.modconfig.data.option.CycleModOption;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.text.Text;

@Mixin(SelectionListEntry.class)
public abstract class SelectionListEntryMixin<T> extends TooltipListEntry<T> implements CycleOptionFacade<T> {

    @Shadow
    private ImmutableList<T> values;

    @Shadow
    private Function<T, Text> nameProvider;

    @Shadow
    private Consumer<T> saveConsumer;

    private SelectionListEntryMixin() {
        super(null, null);
    }

    @Override
    public List<T> modcfg_getStates() {
        return this.values;
    }

    @Override
    public List<Text> modcfg_getStateNames() {
        Function<T, Text> nameProvider = this.nameProvider;
        List<Text> stateNames = new ArrayList<>();
        for(T t : this.values) {
            stateNames.add(nameProvider.apply(t));
        }
        return stateNames;
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
    public Consumer<T> modcfg_getSaveHandler() {
        return this.saveConsumer;
    }

    @Override
    public CycleModOption<T> modcfg_createOption(Text modName, Text categoryName) {
        return new CycleModOption<>(modName, categoryName, this);
    }
}
