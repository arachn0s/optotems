package com.arachnos.optotems.mixin;

import com.arachnos.optotems.TotemHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(LivingEntity.class)

public class TotemActivaterMixin {
@Inject(method = "checkTotemDeathProtection", at = @At("RETURN"), cancellable = true)
    private void optotems$activateCustomTotems(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir){
    LivingEntity entity = (LivingEntity) (Object) this;
    if (entity instanceof ServerPlayer player){
        if (TotemHandler.tryPopCustomTotem(player)){
            cir.setReturnValue(true);
        }
    }
}
}
