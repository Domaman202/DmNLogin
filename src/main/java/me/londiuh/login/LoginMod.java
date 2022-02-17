package me.londiuh.login;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class LoginMod implements ModInitializer {
    public static GetPlayer getPlayer = new GetPlayer();

    @Override
    public void onInitialize() {
        RegisteredPlayersJson.read();
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("login")
                    .then(argument("password", StringArgumentType.word())
                            .executes(ctx -> {
                                String password = StringArgumentType.getString(ctx, "password");
                                String username = ctx.getSource().getPlayer().getEntityName();
                                ServerPlayerEntity player = ctx.getSource().getPlayer();

                                if (!RegisteredPlayersJson.isPlayerRegistered(username)) {
                                    ctx.getSource().sendFeedback(new LiteralText("§cYou're not registered! Use /register instead."), false);
                                } else if (RegisteredPlayersJson.isCorrectPassword(username, password)) {
                                    PlayerLogin playerLogin = LoginMod.getPlayer(ctx.getSource().getPlayer());
                                    playerLogin.loggedIn = true;
                                    ctx.getSource().sendFeedback(new LiteralText("§aLogged in."), false);
                                    if (!player.isCreative()) {
                                        player.setInvulnerable(false);
                                    }
                                    player.networkHandler.sendPacket(new PlaySoundIdS2CPacket(new Identifier("minecraft:block.note_block.pling"), SoundCategory.MASTER, player.getPos(), 100f, 0f));
                                } else {
                                    player.networkHandler.sendPacket(new PlaySoundIdS2CPacket(new Identifier("minecraft:entity.zombie.attack_iron_door"), SoundCategory.MASTER, player.getPos(), 100f, 0.5f));
                                    ctx.getSource().sendFeedback(new LiteralText("§cIncorrect password!"), false);
                                }
                                return 1;
                            })));

            dispatcher.register(literal("register")
                    .then(argument("newPassword", StringArgumentType.word())
                            .then(argument("confirmPassword", StringArgumentType.word())
                                    .executes(ctx -> {
                                        String password = StringArgumentType.getString(ctx, "newPassword");
                                        ServerPlayerEntity player = ctx.getSource().getPlayer();
                                        String username = player.getEntityName();
                                        if (RegisteredPlayersJson.isPlayerRegistered(username)) {
                                            ctx.getSource().sendFeedback(new LiteralText("§cYou're already registered! Use /login instead."), false);
                                            return 1;
                                        }
                                        if (!password.equals(StringArgumentType.getString(ctx, "confirmPassword"))) {
                                            ctx.getSource().sendFeedback(new LiteralText("§cPasswords don't match! Repeat it correctly."), false);
                                            return 1;
                                        }
                                        String uuid = ctx.getSource().getPlayer().getUuidAsString();
                                        RegisteredPlayersJson.save(uuid, username, password);
                                        PlayerLogin playerLogin = LoginMod.getPlayer(ctx.getSource().getPlayer());
                                        playerLogin.loggedIn = true;
                                        player.setInvulnerable(false);
                                        ctx.getSource().sendFeedback(new LiteralText("§aSuccessfully registered."), false);
                                        return 1;
                                    }))));
        });
    }

    public static PlayerLogin getPlayer(ServerPlayerEntity player) {
        return getPlayer.get(player);
    }
}
