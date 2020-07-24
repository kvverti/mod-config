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
    private int intValue = 3;
    private String strValue = "hello";
    private int intFieldValue = 0;

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
            category.addEntry(builder.entryBuilder()
                .startIntSlider(new LiteralText("Int Slider"), intValue, 0, 7)
                .setDefaultValue(3)
                .setSaveConsumer(value -> intValue = value)
                .build());
            category.addEntry(builder.entryBuilder()
                .startTextField(new LiteralText("Text Field"), strValue)
                .setDefaultValue("hello")
                .setSaveConsumer(value -> strValue = value)
                .build());
            category.addEntry(builder.entryBuilder()
                .startIntField(new LiteralText("Int Field"), intFieldValue)
                .setDefaultValue(0)
                .setSaveConsumer(value -> intFieldValue = value)
                .build());
            builder.setSavingRunnable(() -> {
                System.out.println(boolValue);
                System.out.println(enumValue);
                System.out.println(intValue);
                System.out.println(strValue);
                System.out.println(intFieldValue);
            });
            return builder.setParentScreen(parent).build();
        };
    }
}
