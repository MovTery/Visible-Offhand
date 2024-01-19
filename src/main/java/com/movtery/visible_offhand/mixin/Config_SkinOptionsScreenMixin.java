package com.movtery.visible_offhand.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.movtery.visible_offhand.client.VisibleOffhandClient.getConfig;

@Environment(EnvType.CLIENT)
@Mixin(SkinOptionsScreen.class)
public abstract class Config_SkinOptionsScreenMixin extends GameOptionsScreen {
    public Config_SkinOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void newButtons(CallbackInfo ci) {
        ci.cancel();

        int i = 0;
        PlayerModelPart[] var2 = PlayerModelPart.values();

        for (PlayerModelPart playerModelPart : var2) {
            this.addDrawableChild(CyclingButtonWidget.onOffBuilder(this.gameOptions.isPlayerModelPartEnabled(playerModelPart)).build(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, playerModelPart.getOptionName(), (button, enabled) -> this.gameOptions.togglePlayerModelPart(playerModelPart, enabled)));
            ++i;
        }

        this.addDrawableChild(this.gameOptions.getMainArm().createButton(this.gameOptions, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150));
        ++i;
        if (i % 2 == 1) {
            ++i;
        }

        //创建一个新的按钮，用于控制开关双手显示
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(onOrOff(getConfig().getOptions().doubleHands), onOrOff(!getConfig().getOptions().doubleHands))
                .build(this.width / 2 - 102, this.height / 6 + 24 * (i >> 1), 100, 20, Text.translatable("button.vo.double_hands"), (button, enabled) -> {
                    getConfig().getOptions().doubleHands = !getConfig().getOptions().doubleHands;
                    getConfig().save();
                }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 + 2, this.height / 6 + 24 * (i >> 1), 100, 20, ScreenTexts.DONE, (button) -> {
            if (this.client != null) {
                this.client.setScreen(this.parent);
            }
        }));
    }

    @Unique
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
