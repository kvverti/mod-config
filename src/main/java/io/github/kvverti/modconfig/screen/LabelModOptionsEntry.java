package io.github.kvverti.modconfig.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * A section header for grouping options.
 */
public class LabelModOptionsEntry extends ModOptionsEntry {

    private final TextRenderer renderer;
    private final Text label;

    public LabelModOptionsEntry(TextRenderer renderer, Text label) {
        this.renderer = renderer;
        this.label = label;
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        int width = renderer.getWidth(label);
        int posX = x + (entryWidth / 2) - (width / 2);
        int posY = y + (entryHeight / 2) - (renderer.fontHeight / 2);
        renderer.drawWithShadow(matrices, label, posX, posY, 0xffffff);
    }
}
