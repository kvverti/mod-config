package io.github.kvverti.modconfigtest;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;

import net.minecraft.text.LiteralText;

public class ModConfigTest implements ModMenuApi {

    private enum TestEnum { A, B, C }

    private boolean boolValue = false;
    private TestEnum enumValue = TestEnum.A;

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create();
            ConfigCategory category = builder.getOrCreateCategory(new LiteralText("Category"));
            category.addEntry(builder.entryBuilder()
                .startBooleanToggle(new LiteralText("Boolean Value"), boolValue)
                .setDefaultValue(false)
                .setSaveConsumer(value -> boolValue = value)
                .build());
            category.addEntry(builder.entryBuilder()
                .startEnumSelector(new LiteralText("Enum Value"), TestEnum.class, enumValue)
                .setDefaultValue(TestEnum.A)
                .setSaveConsumer(value -> enumValue = value)
                .build());
            builder.setSavingRunnable(() -> {
                System.out.println(boolValue);
                System.out.println(enumValue);
            });
            return builder.setParentScreen(parent).build();
        };
    }
}
