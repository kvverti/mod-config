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
    private Function<Integer, Text> textGetter;

    @Shadow
    public abstract Integer getValue();

    private IntegerSliderEntryMixin() {
        super(null, null);
    }

    @Override
    public long modcfg_getMin() {
        return this.minimum;
    }

    @Override
    public long modcfg_getMax() {
        return this.maximum;
    }

    @Override
    public Text modcfg_getOptionName() {
        return this.getFieldName();
    }

    @Override
    public Long modcfg_getValue() {
        return this.getValue().longValue();
    }

    @Override
    public Function<Long, Text> modcfg_getNameProvider() {
        Function<Integer, Text> f = this.textGetter;
        return v -> f.apply(v.intValue());
    }

    @Override
    public Consumer<Long> modcfg_getSaveHandler() {
        Consumer<Integer> s = this.saveConsumer;
        return v -> s.accept(v.intValue());
    }
}
