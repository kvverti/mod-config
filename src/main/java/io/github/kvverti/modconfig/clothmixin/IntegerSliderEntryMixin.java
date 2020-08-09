package io.github.kvverti.modconfig.clothmixin;

import java.util.function.Consumer;
import java.util.function.Function;

import io.github.kvverti.modconfig.data.facade.IntOptionFacade;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.text.Text;

@Mixin(IntegerSliderEntry.class)
public abstract class IntegerSliderEntryMixin extends TooltipListEntry<Integer> implements IntOptionFacade {

    @Shadow
    private int minimum;

    @Shadow
    private int maximum;

    @Shadow
    private Consumer<Integer> saveConsumer;

    @Shadow
    public abstract Integer getValue();

    @Shadow
    public abstract Function<Integer, Text> getTextGetter();

    private IntegerSliderEntryMixin() {
        super(null, null);
    }

    @Override
    public int modcfg_getMin() {
        return this.minimum;
    }

    @Override
    public int modcfg_getMax() {
        return this.maximum;
    }

    @Override
    public Text modcfg_getOptionName() {
        return this.getFieldName();
    }

    @Override
    public Integer modcfg_getValue() {
        return this.getValue();
    }

    @Override
    public Function<Integer, Text> modcfg_getNameProvider() {
        return this.getTextGetter();
    }

    @Override
    public Consumer<Integer> modcfg_getSaveHandler() {
        return this.saveConsumer;
    }
}
