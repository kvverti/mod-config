package io.github.kvverti.modconfig.mixin;

import io.github.kvverti.modconfig.iface.ClearFocus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.AbstractButtonWidget;

@Mixin(AbstractButtonWidget.class)
public abstract class AbstractButonWidgetMixin extends DrawableHelper implements ClearFocus {

    @Shadow
    protected abstract void onFocusedChanged(boolean bl);

    @Shadow
    protected abstract void setFocused(boolean focused);

    @Shadow
    public abstract boolean isFocused();

    @Unique
    private void clearFocus() {
        if(this.isFocused()) {
            this.setFocused(false);
            this.onFocusedChanged(false);
        }
    }

    @Override
    public void modcfg_super_clearFocus() {
        clearFocus();
    }

    @Override
    public void modcfg_clearFocus() {
        clearFocus();
    }
}
