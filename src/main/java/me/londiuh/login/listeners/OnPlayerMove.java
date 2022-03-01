package me.londiuh.login.listeners;

import me.londiuh.login.LoginMod;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class OnPlayerMove {
    public static boolean canMove(ServerPlayNetworkHandler networkHandler) {
        ServerPlayerEntity player = networkHandler.player;
        boolean isLoggedIn = LoginMod.getPlayer(networkHandler.player).get();
        if (!isLoggedIn) {
            player.teleport(player.getX(), player.getY(), player.getZ()); // teleport to sync client position
            if (player.hasVehicle()) {
                Entity vehicle = player.getVehicle();
                vehicle.teleport(player.getX(), vehicle.getY(), player.getZ());
            }
        }
        return isLoggedIn;
    }
}
