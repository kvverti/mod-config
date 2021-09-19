package io.github.kvverti.modconfig.mixin;

import io.github.kvverti.modconfig.screen.ModOptionsScreen;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;

@Mixin({ TitleScreen.class, GameMenuScreen.class })
public abstract class TitleAndPauseScreenMixin extends Screen {
    private TitleAndPauseScreenMixin() {
        super(null);
    }

    @Dynamic("Options button click handlers")
    @ModifyArg(
        method = {
            "method_19863(Lnet/minecraft/client/gui/widget/ButtonWidget;)V",
            "method_19840(Lnet/minecraft/client/gui/widget/ButtonWidget;)V"
        },
        index = 0,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"
        )
    )
    private Screen replaceOptionsWithModConfigOptionsScreen(Screen screen) {
        return new ModOptionsScreen(this);
    }
}
