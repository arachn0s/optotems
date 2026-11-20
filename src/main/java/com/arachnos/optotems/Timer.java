package com.arachnos.optotems;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Timer {
    private static final Map<UUID,Integer> ghostTimers = new HashMap<>();
    public static void addGhost(ServerPlayer player) {
        ghostTimers.put(player.getUUID(),600);
    }
        public static void register(){
            ServerTickEvents.END_SERVER_TICK.register(server -> {
                ghostTimers.entrySet().removeIf(entry -> {
                    UUID uuid = entry.getKey();
                    int ticksLeft = entry.getValue() - 1;
                    ServerPlayer player = server.getPlayerList().getPlayer(uuid);
                    if(player == null){
                        return true;
                    }
                    if (ticksLeft <= 0){
                        player.setGameMode(GameType.SURVIVAL);
                        return true;
                    }
                    entry.setValue(ticksLeft);
                    return  false;
                });
            });
        }
}
