package me.londiuh.login;

import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerLogin {
    public final ServerPlayerEntity player;
    public boolean loggedIn;

    public PlayerLogin(ServerPlayerEntity player) {
        this.player = player;
    }
}
