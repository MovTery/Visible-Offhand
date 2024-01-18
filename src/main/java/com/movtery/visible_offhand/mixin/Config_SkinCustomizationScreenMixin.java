package com.movtery.visible_offhand.mixin;

import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SkinCustomizationScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.movtery.visible_offhand.VisibleOffhand.getConfig;

@Mixin(SkinCustomizationScreen.class)
public class Config_SkinCustomizationScreenMixin extends OptionsSubScreen {

    public Config_SkinCustomizationScreenMixin(Screen pLastScreen, Options pOptions, Component pTitle) {
        super(pLastScreen, pOptions, pTitle);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void newButtons(CallbackInfo ci) {
        ci.cancel();

        int i = 0;

        for (PlayerModelPart playermodelpart : PlayerModelPart.values()) {
            this.addRenderableWidget(
                    CycleButton.onOffBuilder(this.options.isModelPartEnabled(playermodelpart))
                            .create(
                                    this.width / 2 - 155 + i % 2 * 160,
                                    this.height / 6 + 24 * (i >> 1),
                                    150,
                                    20,
                                    playermodelpart.getName(),
                                    (p_169436_, p_169437_) -> this.options.toggleModelPart(playermodelpart, p_169437_)
                            )
            );
            ++i;
        }

        this.addRenderableWidget(this.options.mainHand().createButton(this.options, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150));
        if (++i % 2 == 1) {
            ++i;
        }

        //创建一个新的按钮，用于控制开关双手显示
        this.addRenderableWidget(CycleButton.booleanBuilder(visibleOffhandNeoforged$onOrOff(getConfig().getOptions().doubleHands), visibleOffhandNeoforged$onOrOff(!getConfig().getOptions().doubleHands))
                .create(this.width / 2 - 102, this.height / 6 + 24 * (i >> 1), 100, 20, Component.translatable("button.vo.double_hands"), (button, enabled) -> {
                    getConfig().getOptions().doubleHands = !getConfig().getOptions().doubleHands;
                    getConfig().save();
                }));

        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_DONE, p_280828_ -> {
                            if (this.minecraft != null) {
                                this.minecraft.setScreen(this.lastScreen);
                            }
                        })
                        .bounds(this.width / 2 + 2, this.height / 6 + 24 * (i >> 1), 100, 20)
                        .build()
        );
    }

    @Unique
    private Component visibleOffhandNeoforged$onOrOff(Boolean button) {
        Component component;
        if (button) {
            component = Component.translatable("button.vo.on");
        } else {
            component = Component.translatable("button.vo.off");
        }
        return component;
    }
}
