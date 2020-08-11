package io.github.kvverti.modconfig.data.option.widget;

import net.minecraft.client.util.math.MatrixStack;

/**
 * A widget that renders on the overlay (second) layer.
 */
public interface OverlayRenderable {

    void renderOverlay(MatrixStack matrices, int mouseX, int mouseY, float delta);
}
