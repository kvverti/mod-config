package io.github.kvverti.modconfig.data;

import net.minecraft.client.gui.screen.Screen;

@FunctionalInterface
public interface ScreenFactory {
    Screen create(Screen parent);
}
