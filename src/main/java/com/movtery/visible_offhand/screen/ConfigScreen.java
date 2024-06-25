package com.movtery.visible_offhand.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import static com.movtery.visible_offhand.VisibleOffhand.getConfig;
import static com.movtery.visible_offhand.VisibleOffhand.reloadConfig;

public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Component.translatable("mod.vo.name"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        //创建一个新的按钮，用于控制开关双手显示
        this.addRenderableWidget(CycleButton.booleanBuilder(visibleOffhandForge$onOrOff(getConfig().getOptions().doubleHands), visibleOffhandForge$onOrOff(!getConfig().getOptions().doubleHands))
                .create(this.width / 2 - 112, this.height / 2, 110, 20, Component.translatable("button.vo.double_hands"), (button, enabled) -> {
                    getConfig().getOptions().doubleHands = !getConfig().getOptions().doubleHands;
                    getConfig().save();
                }));

        this.addRenderableWidget(new Button(this.width / 2 + 2, this.height / 2, 110, 20, Component.translatable("button.vo.reload_config"), (button) -> {
            reloadConfig();
            if (this.minecraft != null) {
                this.minecraft.setScreen(this.parent);
            }
        }));
    }

    @Override
    public void render(@NotNull PoseStack p_96562_, int p_96563_, int p_96564_, float p_96565_) {
        this.renderBackground(p_96562_);
        drawCenteredString(p_96562_, this.font, this.title, this.width / 2, 20, 16777215);
        super.render(p_96562_, p_96563_, p_96564_, p_96565_);
    }

    private Component visibleOffhandForge$onOrOff(Boolean button) {
        Component component;
        if (button) {
            component = Component.translatable("button.vo.on");
        } else {
            component = Component.translatable("button.vo.off");
        }
        return component;
    }
}