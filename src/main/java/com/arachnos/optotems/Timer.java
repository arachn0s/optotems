package com.arachnos.optotems;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.telemetry.TelemetryProperty;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Timer {

    private static final Map<UUID, Integer> ghostTimers = new HashMap<>();

    public static void addGhost(ServerPlayer player) {

        ghostTimers.put(player.getUUID(), -1);
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ghostTimers.entrySet().removeIf(entry -> {
                UUID uuid = entry.getKey();
                int stateOrTime = entry.getValue();

                ServerPlayer player = server.getPlayerList().getPlayer(uuid);
                if (player == null) {
                    return true;
                }


                if (stateOrTime == -1) {
                    player.setGameMode(GameType.SPECTATOR);
                    entry.setValue(600);
                    return false;
                }


                int ticksLeft = stateOrTime - 1;
                if (ticksLeft <= 0) {
                    player.setGameMode(GameType.SURVIVAL);
                    return true;
                }

                entry.setValue(ticksLeft);
                return false;
            });
        });
    }
}