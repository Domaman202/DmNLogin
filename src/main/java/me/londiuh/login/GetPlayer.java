package me.londiuh.login;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class GetPlayer extends HashMap<UUID, AtomicBoolean> {
    public AtomicBoolean get(ServerPlayerEntity player) {
        UUID uuid = player.getUuid();
        if (containsKey(uuid)) {
            return super.get(uuid);
        }
        AtomicBoolean b = new AtomicBoolean(false);
        put(uuid, b);
        return b;
    }
}
