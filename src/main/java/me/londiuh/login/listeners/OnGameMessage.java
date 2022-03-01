package me.londiuh.login.listeners;

import me.londiuh.login.LoginMod;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.concurrent.atomic.AtomicBoolean;

public class OnGameMessage {
    public static boolean canSendMessage(ServerPlayNetworkHandler networkHandler, ChatMessageC2SPacket packet) {
        ServerPlayerEntity player = networkHandler.player;
        AtomicBoolean playerLogin = LoginMod.getPlayer(player);
        String message = packet.getChatMessage();
        // TODO: config to allow more commands when you're not logged
        if (!playerLogin.get() && (message.startsWith("/login") || message.startsWith("/register"))) {
            return true;
        }
        return playerLogin.get();
    }
}
