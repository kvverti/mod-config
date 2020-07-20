package io.github.kvverti.modconfig.data;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class NestedScreenModOption extends ModOption {

    private final ScreenFactory factory;

    public NestedScreenModOption(Text optionName, ScreenFactory factory) {
        super(optionName);
        this.factory = factory;
    }

    @Override
    public void onInteract(Screen parent) {
        MinecraftClient.getInstance().openScreen(factory.create(parent));
    }
}
