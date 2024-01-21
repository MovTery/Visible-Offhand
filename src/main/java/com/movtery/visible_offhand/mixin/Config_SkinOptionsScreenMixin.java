package com.movtery.visible_offhand.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.movtery.visible_offhand.client.VisibleOffhandClient.getConfig;

@Environment(EnvType.CLIENT)
@Mixin(SkinOptionsScreen.class)
public abstract class Config_SkinOptionsScreenMixin extends GameOptionsScreen {
    @Shadow
    protected abstract Text getPlayerModelPartDisplayString(PlayerModelPart part);

    public Config_SkinOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void newButtons(CallbackInfo ci) {
        ci.cancel();

        int i = 0;
        PlayerModelPart[] var2 = PlayerModelPart.values();

        for (PlayerModelPart playerModelPart : var2) {
            this.addButton(new ButtonWidget(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, this.getPlayerModelPartDisplayString(playerModelPart), (button) -> {
                this.gameOptions.togglePlayerModelPart(playerModelPart);
                button.setMessage(this.getPlayerModelPartDisplayString(playerModelPart));
            }));
            ++i;
        }

        this.addButton(new OptionButtonWidget(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, Option.MAIN_HAND, Option.MAIN_HAND.getMessage(this.gameOptions), (button) -> {
            Option.MAIN_HAND.cycle(this.gameOptions, 1);
            this.gameOptions.write();
            button.setMessage(Option.MAIN_HAND.getMessage(this.gameOptions));
            this.gameOptions.sendClientSettings();
        }));
        ++i;
        if (i % 2 == 1) {
            ++i;
        }

        //创建一个新的按钮，用于控制开关双手显示
        this.addButton(new ButtonWidget(this.width / 2 - 102, this.height / 6 + 24 * (i >> 1), 100, 20, onOrOff(getConfig().getOptions().doubleHands), (button) -> {
                    getConfig().getOptions().doubleHands = !getConfig().getOptions().doubleHands;
                    getConfig().save();
                    button.setMessage(onOrOff(getConfig().getOptions().doubleHands));
                }));

        this.addButton(new ButtonWidget(this.width / 2 + 2, this.height / 6 + 24 * (i >> 1), 100, 20, ScreenTexts.DONE, (button) -> {
            if (this.client != null) {
                this.client.openScreen(this.parent);
            }
        }));
    }

    @Unique
    private Text onOrOff(Boolean button) {
        TranslatableText translatableText;
        if (button) {
            translatableText = (TranslatableText) new TranslatableText("button.vo.double_hands").append(":").append(new TranslatableText("button.vo.on"));
        } else {
            translatableText = (TranslatableText) new TranslatableText("button.vo.double_hands").append(":").append(new TranslatableText("button.vo.off"));
        }
        return translatableText;
    }
}
