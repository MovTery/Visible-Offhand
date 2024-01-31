package com.movtery.visible_offhand.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import static com.movtery.visible_offhand.client.VisibleOffhandClient.getConfig;

public class ConfigScreen extends Screen {
    private final Screen parent;

    protected ConfigScreen(Screen parent) {
        super(Text.translatable("modmenu.nameTranslation.visible_offhand"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        //创建一个新的按钮，用于控制开关双手显示
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(onOrOff(getConfig().getOptions().doubleHands), onOrOff(!getConfig().getOptions().doubleHands))
                .build(this.width / 2 - 112, this.height / 2, 110, 20, Text.translatable("button.vo.double_hands"), (button, enabled) -> {
                    getConfig().getOptions().doubleHands = !getConfig().getOptions().doubleHands;
                    getConfig().save();
                }));

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            if (this.client != null) {
                this.client.setScreen(this.parent);
            }
        }).dimensions(this.width / 2 + 2, this.height / 2, 110, 20).build());

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 30, 16777215);

        super.render(context, mouseX, mouseY, delta);
    }

    private Text onOrOff(Boolean button) {
        Text text;
        if (button) {
            text = Text.translatable("button.vo.on");
        } else {
            text = Text.translatable("button.vo.off");
        }
        return text;
    }
}
