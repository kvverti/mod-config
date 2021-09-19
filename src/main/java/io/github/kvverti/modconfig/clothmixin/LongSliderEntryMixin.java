package io.github.kvverti.modconfig.clothmixin;

import java.util.function.Consumer;
import java.util.function.Function;

import io.github.kvverti.modconfig.data.facade.IntOptionFacade;
import me.shedaniel.clothconfig2.gui.entries.LongSliderEntry;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.text.Text;

@Mixin(LongSliderEntry.class)
public abstract class LongSliderEntryMixin extends TooltipListEntry<Long> implements IntOptionFacade {

    @Shadow(remap = false)
    private long minimum;

    @Shadow(remap = false)
    private long maximum;

    @Final
    @Shadow(remap = false)
    private Consumer<Long> saveConsumer;

    @Shadow(remap = false)
    private Function<Long, Text> textGetter;

    @Shadow(remap = false)
    public abstract Long getValue();

    private LongSliderEntryMixin() {
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
        return this.getValue();
    }

    @Override
    public Function<Long, Text> modcfg_getNameProvider() {
        return this.textGetter;
    }

    @Override
    public Consumer<Long> modcfg_getSaveHandler() {
        return this.saveConsumer;
    }
}
