package me.londiuh.login;

import me.londiuh.login.commands.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.concurrent.atomic.AtomicBoolean;

public class LoginMod implements ModInitializer {
    static GetPlayer getPlayer = new GetPlayer();

    @Override
    public void onInitialize() {
        RegisteredPlayersJson.read();
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            LoginCommand.register(dispatcher);
            RegisterCommand.register(dispatcher);
        });
    }

    public static AtomicBoolean getPlayer(ServerPlayerEntity player) {
        return getPlayer.get(player);
    }
}
