package com.movtery.visible_offhand.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.movtery.visible_offhand.client.VisibleOffhandClient.getConfig;

@Environment(EnvType.CLIENT)
@Mixin(SkinOptionsScreen.class)
public abstract class Config_SkinOptionsScreenMixin extends Screen {
    //创建一个新的按钮，用于控制开关双手显示
    @Unique
    public CyclingButtonWidget<Boolean> doubleHands;

    protected Config_SkinOptionsScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);

    @Inject(method = "init", at = @At("HEAD"))
    private void newButtons(CallbackInfo ci) {
        doubleHands = CyclingButtonWidget.onOffBuilder(onOrOff(getConfig().getOptions().doubleHands), onOrOff(!getConfig().getOptions().doubleHands))
                .build(10, 10, 100, 20, Text.translatable("button.vo.double_hands"), (button, enabled) -> {
                    getConfig().getOptions().doubleHands = !getConfig().getOptions().doubleHands;
                    getConfig().save();
                });
        this.addDrawableChild(doubleHands);
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
