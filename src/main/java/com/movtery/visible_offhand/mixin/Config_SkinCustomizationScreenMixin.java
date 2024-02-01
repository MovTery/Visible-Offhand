package com.movtery.visible_offhand.mixin;

import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SkinCustomizationScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.movtery.visible_offhand.VisibleOffhand.getConfig;
import static com.movtery.visible_offhand.VisibleOffhand.reloadConfig;

@OnlyIn(Dist.CLIENT)
@Mixin(SkinCustomizationScreen.class)
public class Config_SkinCustomizationScreenMixin extends OptionsSubScreen {
    public Config_SkinCustomizationScreenMixin(Screen p_96284_, Options p_96285_, Component p_96286_) {
        super(p_96284_, p_96285_, p_96286_);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void newButton(CallbackInfo ci) {
        ci.cancel();
        int i = 0;

        for (PlayerModelPart playermodelpart : PlayerModelPart.values()) {
            this.addRenderableWidget(CycleButton.onOffBuilder(this.options.isModelPartEnabled(playermodelpart)).create(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, playermodelpart.getName(), (p_169436_, p_169437_) -> {
                this.options.toggleModelPart(playermodelpart, p_169437_);
            }));
            ++i;
        }

        this.addRenderableWidget(Option.MAIN_HAND.createButton(this.options, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150));
        ++i;
        if (i % 2 == 1) {
            ++i;
        }

        //创建一个新的按钮，用于控制开关双手显示
        this.addRenderableWidget(CycleButton.booleanBuilder(visibleOffhandForge$onOrOff(getConfig().getOptions().doubleHands), visibleOffhandForge$onOrOff(!getConfig().getOptions().doubleHands))
                .create(this.width / 2 - 102, this.height / 6 + 24 * (i >> 1), 100, 20, new TranslatableComponent("button.vo.double_hands"), (button, enabled) -> {
                    getConfig().getOptions().doubleHands = !getConfig().getOptions().doubleHands;
                    getConfig().save();
                }));

        this.addRenderableWidget(new Button(this.width / 2 + 2, this.height / 6 + 24 * (i >> 1), 100, 20, new TranslatableComponent("button.vo.reload_config"), (p_96700_) -> {
            reloadConfig();
            if (this.minecraft != null) {
                this.minecraft.setScreen(this.lastScreen);
            }
        }));
    }

    @Unique
    private Component visibleOffhandForge$onOrOff(Boolean button) {
        Component component;
        if (button) {
            component = new TranslatableComponent("button.vo.on");
        } else {
            component = new TranslatableComponent("button.vo.off");
        }
        return component;
    }
}
