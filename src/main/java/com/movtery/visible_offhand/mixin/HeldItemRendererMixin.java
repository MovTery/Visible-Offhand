package com.movtery.visible_offhand.mixin;

import com.movtery.visible_offhand.config.Settings;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class DoubleHands_HeldItemRendererMixin {
    @Shadow
    protected abstract void renderArmHoldingItem(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm);

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    private void doubleHands(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (Settings.isDoubleHands() && !Settings.isForcedRenderingHand()) {
            boolean mainHand = hand == Hand.MAIN_HAND;
            Arm offArm = mainHand ? player.getMainArm() : player.getMainArm().getOpposite();
            if (item.isEmpty() && (!mainHand && !player.isInvisible())) {
                this.renderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, offArm);
            }
        }
    }
}
