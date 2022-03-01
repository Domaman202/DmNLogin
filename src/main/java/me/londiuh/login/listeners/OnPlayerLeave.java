package me.londiuh.login.listeners;

import me.londiuh.login.LoginMod;
import net.minecraft.server.network.ServerPlayerEntity;

public class OnPlayerLeave {
    public static void listen(ServerPlayerEntity player) {
        LoginMod.getPlayer(player).set(false);
    }
}
