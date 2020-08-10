package io.github.kvverti.modconfig.clothmixin;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import io.github.kvverti.modconfig.data.facade.DropdownFacade;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.text.Text;

@Mixin(DropdownBoxEntry.class)
public abstract class DropdownBoxEntryMixin<T> extends TooltipListEntry<T> implements DropdownFacade<T> {

    @Shadow
    @Nullable
    private Consumer<T> saveConsumer;

    @Shadow
    protected DropdownBoxEntry.SelectionElement<T> selectionElement;

    private DropdownBoxEntryMixin(Text fieldName, Supplier<Optional<Text[]>> tooltipSupplier) {
        super(fieldName, tooltipSupplier);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> modcfg_getSelections() {
        return ((SelectionElementAccessor<T>)this.selectionElement).getMenu().getSelections();
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
        return DropdownFacade.super.modcfg_getNameProvider();
    }

    @Override
    public Consumer<T> modcfg_getSaveHandler() {
        if(this.saveConsumer == null) {
            return v -> {
            };
        } else {
            return this.saveConsumer;
        }
    }
}
