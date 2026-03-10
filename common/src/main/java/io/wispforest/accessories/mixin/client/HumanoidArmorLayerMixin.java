package io.wispforest.accessories.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.EquipmentChecking;
import io.wispforest.accessories.data.EntitySlotLoader;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {

    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    private void accessories$hideHelmetWhenHatCosmeticIsVisible(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            LivingEntity livingEntity,
            EquipmentSlot equipmentSlot,
            int packedLight,
            HumanoidModel<?> humanoidModel,
            CallbackInfo ci
    ) {
        // If invisible, keep vanilla behaviour
        if (livingEntity.hasEffect(MobEffects.INVISIBILITY)) return;

        // Case: Rendering of the helmet
        if (equipmentSlot == EquipmentSlot.HEAD) {

            var capability = AccessoriesCapability.get(livingEntity);
            if (capability == null) return;

            //if (!capability.isEquipped(stack -> !stack.isEmpty(), EquipmentChecking.ACCESSORIES_ONLY)) return;

            for (var container : capability.getContainers().values()) {

                // Cancel rendering of the helmet if there a hat accessorie is equiped
                if (container.getSlotName().equals("hat") && !container.getAccessories().isEmpty()) {
                    ci.cancel();
                    return;
                }
            }
        }

        //EntitySlotLoader.getEntitySlots(livingEntity);

        /*for (var entry : capability.getContainers().entrySet()) {
            if (!entry.getValue().slotType().name().equals("hat")) continue;
            System.out.println("HELMET + HAT");
            var cosmetics = entry.getValue().getCosmeticAccessories();

            ci.cancel();
            System.out.println("CANCELLLLLLLLLLLLLLLLLLLLLLLLLL" + cosmetics);
            return;
            for (int i = 0; i < cosmetics.getContainerSize(); i++) {
                if (!cosmetics.getItem(i).isEmpty()) {
                    ci.cancel();
                    System.out.println("CANCELLLLLLLLLLLLLLLLLLLLLLLLLL");
                    return;
                }
            }
        }*/
    }
}