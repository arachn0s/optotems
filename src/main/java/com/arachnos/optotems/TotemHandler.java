package com.arachnos.optotems;

import com.arachnos.optotems.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class TotemHandler {
    public static boolean tryPopCustomTotem(ServerPlayer player) {
        ServerLevel world = (ServerLevel) player.level();
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack istotem = player.getItemInHand(hand);
            if (istotem.is(ModItems.TOTEM_OF_THE_GHOST)) {
                player.setGameMode(GameType.SPECTATOR);
                Timer.addGhost(player);
                return true;
            } else if (istotem.is(ModItems.TOTEM_OF_WOLFGANG)) {
                spawnWolves(player, world);
                return true;
            } else if (istotem.is(ModItems.TOTEM_OF_THUNDER)) {
                return true;
            }
        }
        return false;
    }

    private static void spawnWolves(ServerPlayer player, ServerLevel world){
        for(int i = 1; i <=20; i++){
        Wolf wolf = EntityType.WOLF.create(world, EntitySpawnReason.COMMAND);
            if (wolf!= null){
                double player_x = player.getX();
                double player_z = player.getZ()+1;
                double player_y = player.getRandomY();
                wolf.teleportTo(player_x,player_y,player_z);
                wolf.tame(player);
                wolf.addEffect(new MobEffectInstance(MobEffects.SPEED, 1800, 1));
                wolf.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 1800, 1));
                wolf.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 9600, 0));
                world.addFreshEntity(wolf);
            }
        }
    }
}