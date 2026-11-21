package com.arachnos.optotems.mixin;

import com.arachnos.optotems.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LivingEntity.class)
public class EvokerMixin {
    private static final Random RANDOM = new Random();

    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"), cancellable = true)
    private void overrideEvokerDeathDrops(ServerLevel serverLevel, DamageSource damageSource, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;


        if (entity.getType().toString().contains("evoker")) {
            int roll = RANDOM.nextInt(100);
            ItemStack chosenTotem;

            if (roll < 60) {
                chosenTotem = new ItemStack(Items.TOTEM_OF_UNDYING, 1);
            } else if (roll < 92) {
                chosenTotem = new ItemStack(ModItems.TOTEM_OF_THUNDER, 1);
            } else if (roll < 97) {
                chosenTotem = new ItemStack(ModItems.TOTEM_OF_THE_GHOST, 1);
            } else if (roll <=100) {
                chosenTotem = new ItemStack(ModItems.TOTEM_OF_WOLFGANG, 1);
            } else{
                chosenTotem = new ItemStack(ModItems.TOTEM_OF_WOLFGANG,1);
            }
            entity.spawnAtLocation(serverLevel, chosenTotem);
            entity.spawnAtLocation(serverLevel,new ItemStack(Items.EMERALD));
            ci.cancel();
        }
    }
}