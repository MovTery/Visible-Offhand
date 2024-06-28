package com.movtery.visible_offhand.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.movtery.visible_offhand.VisibleOffhandClient.getConfig;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Shadow
    protected abstract void renderPlayerArm(PoseStack pPoseStack, MultiBufferSource pBuffer, int pCombinedLight, float pEquippedProgress, float pSwingProgress, HumanoidArm pSide);

    @Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.AFTER))
    private void renderArmWithItem(AbstractClientPlayer abstractClientPlayer, float f, float g, InteractionHand interactionHand, float swingProgress, ItemStack itemStack, float equippedProgress, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, CallbackInfo ci) {
        if (getConfig().getOptions().doubleHands) {
            boolean mainHand = interactionHand == InteractionHand.MAIN_HAND;
            Item mainHandItem = abstractClientPlayer.getMainHandItem().getItem();
            String mainHandItemId = BuiltInRegistries.ITEM.getKey(mainHandItem).toString();
            HumanoidArm offArm = mainHand ? abstractClientPlayer.getMainArm() : abstractClientPlayer.getMainArm().getOpposite();
            if (itemStack.isEmpty() && !getConfig().getOptions().handheldItems.contains(mainHandItemId) && (!mainHand && !abstractClientPlayer.isInvisible())) {
                this.renderPlayerArm(poseStack, multiBufferSource, combinedLight, equippedProgress, swingProgress, offArm);
            }
        }
    }
}
