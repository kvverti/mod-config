package io.github.kvverti.modconfig.clothmixin;

import java.util.function.Consumer;
import java.util.function.Predicate;

import io.github.kvverti.modconfig.data.facade.TextFieldOptionFacade;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.gui.entries.TextFieldListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.text.Text;

// TODO: implement invalid value detection and prevent saving in this case
// (to implement on the facade or option interfaces)
@Mixin(IntegerListEntry.class)
public abstract class IntegerListEntryMixin extends TextFieldListEntry<Integer> implements TextFieldOptionFacade {

    @Shadow
    private Consumer<Integer> saveConsumer;

    @Shadow
    public abstract Integer getValue();

    private IntegerListEntryMixin() {
        super(null, null, null, () -> null);
    }

    @Override
    public int modcfg_getMaxLength() {
        // ten digits plus sign
        return 11;
    }

    @Override
    public Predicate<String> modcfg_getValidator() {
        return value -> value.matches("[+-]?\\d*");
    }

    @Override
    public boolean modcfg_isShort() {
        return true;
    }

    @Override
    public Text modcfg_getOptionName() {
        return this.getFieldName();
    }

    @Override
    public String modcfg_getValue() {
        return this.getValue().toString();
    }

    @Override
    public Consumer<String> modcfg_getSaveHandler() {
        return value -> {
            try {
                this.saveConsumer.accept(Integer.parseInt(value));
            } catch(NumberFormatException e) {
                // ignored
            }
        };
    }
}
