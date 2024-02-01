package com.movtery.visible_offhand.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.movtery.visible_offhand.client.VisibleOffhandClient.getConfig;

@Environment(EnvType.CLIENT)
@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    @Shadow
    protected abstract void renderArmHoldingItem(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm);

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    private void doubleHands(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (getConfig().getOptions().doubleHands) {
            boolean mainHand = hand == Hand.MAIN_HAND;
            Item mainHandItem = player.getMainHandStack().getItem();
            String mainHandItemId = Registry.ITEM.getId(mainHandItem).toString();
            Arm offArm = mainHand ? player.getMainArm() : player.getMainArm().getOpposite();
            if (item.isEmpty() && !getConfig().getOptions().handheldItems.contains(mainHandItemId) && (!mainHand && !player.isInvisible())) {
                this.renderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, offArm);
            }
        }
    }
}
