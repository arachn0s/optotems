package com.arachnos.optotems;

import com.arachnos.optotems.item.ModItems;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;

public class TotemHandler {
    public static boolean tryPopCustomTotem(ServerPlayer player) {
        ServerLevel world = (ServerLevel) player.level();
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack istotem = player.getItemInHand(hand);
            if (istotem.is(ModItems.TOTEM_OF_THE_GHOST)) {
                istotem.shrink(1);
                world.broadcastEntityEvent(player, (byte) 35);
                player.setHealth(1);
                player.removeAllEffects();
                player.setGameMode(GameType.SPECTATOR);
                ghostTimers.put(player.getUUID(), -1);
                return true;
            } else if (istotem.is(ModItems.TOTEM_OF_WOLFGANG)) {
                istotem.shrink(1);
                world.broadcastEntityEvent(player, (byte) 35);
                player.setHealth(1);
                player.removeAllEffects();
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 800, 0));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                spawnWolves(player, world);
                return true;
            } else if (istotem.is(ModItems.TOTEM_OF_THUNDER)) {
                istotem.shrink(1);
                world.broadcastEntityEvent(player, (byte) 35);
                player.setHealth(1);
                player.removeAllEffects();
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                thunderStrikes(player,world);
                return true;
            }
        }
        return false;
    }
    private static void thunderStrikes(ServerPlayer player ,ServerLevel world) {
        for (int i = 1; i <= 4; i++) {
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(world, EntitySpawnReason.COMMAND);
            if (lightningBolt != null) {
                if (i == 1) {
                    double player_x = player.getX() + 5;
                    double player_z = player.getZ();
                    double player_y = player.getY();
                    lightningBolt.teleportTo(player_x, player_y, player_z);
                    world.addFreshEntity(lightningBolt);
                }
                else if (i == 2) {
                    double player_x = player.getX() - 5;
                    double player_z = player.getZ();
                    double player_y = player.getY();
                    lightningBolt.teleportTo(player_x, player_y, player_z);
                    world.addFreshEntity(lightningBolt);
                }
                else if (i == 3) {
                    double player_x = player.getX();
                    double player_z = player.getZ()+5;
                    double player_y = player.getY();
                    lightningBolt.teleportTo(player_x, player_y, player_z);
                    world.addFreshEntity(lightningBolt);
                } else if (i==4) {
                    double player_x = player.getX();
                    double player_z = player.getZ()-5;
                    double player_y = player.getY();
                    lightningBolt.teleportTo(player_x, player_y, player_z);
                    world.addFreshEntity(lightningBolt);
                }
            }
        }
    }
    private static void spawnWolves(ServerPlayer player, ServerLevel world) {
        for (int i = 1; i <= 20; i++) {
            Wolf wolf = EntityType.WOLF.create(world, EntitySpawnReason.COMMAND);
            if (wolf != null) {
                double player_x = player.getX();
                double player_z = player.getZ() + 1;
                double player_y = player.getY();
                wolf.teleportTo(player_x, player_y, player_z);
                wolf.tame(player);
                wolf.addEffect(new MobEffectInstance(MobEffects.SPEED, 1800, 1));
                wolf.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 1800, 1));
                wolf.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 9600, 0));
                world.addFreshEntity(wolf);
            }
        }
    }

    private static final Map<UUID, Integer> ghostTimers = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            java.util.List<UUID> toRemove = new java.util.ArrayList<>();

            for (Map.Entry<UUID, Integer> entry : ghostTimers.entrySet()) {
                UUID uuid = entry.getKey();
                int stateOrTime = entry.getValue();

                ServerPlayer player = server.getPlayerList().getPlayer(uuid);
                if (player == null) {
                    toRemove.add(uuid);
                    continue;
                }

                if (stateOrTime == -1) {
                    player.setGameMode(GameType.SPECTATOR);
                    ghostTimers.put(uuid, 600);
                    continue;
                }

                int ticksLeft = stateOrTime - 1;
                if (ticksLeft <= 0) {
                    player.setGameMode(GameType.SURVIVAL);
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
                    player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                    toRemove.add(uuid);
                    continue;
                }

                ghostTimers.put(uuid, ticksLeft);
            }

            for (UUID uuid : toRemove) {
                ghostTimers.remove(uuid);
            }
        });
    }
}