package com.arachnos.optotems;

import com.arachnos.optotems.item.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.client.telemetry.TelemetryProperty;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class TotemHandler {
    public static void register(){
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
            if(!(entity instanceof ServerPlayer player)){
                return true;
            }
            for (InteractionHand hand:InteractionHand.values()){
                ItemStack istotem = player.getItemInHand(hand);
                if (istotem.is(ModItems.TOTEM_OF_THE_GHOST)){
                    player.setGameMode(GameType.SPECTATOR);
                    Timer.addGhost(player);
                    break;
                }
            }
            return true;
        });
    }
}
