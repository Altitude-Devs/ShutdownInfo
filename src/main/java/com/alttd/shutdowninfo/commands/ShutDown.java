package com.alttd.shutdowninfo.commands;

import com.alttd.shutdowninfo.ShutdownInfo;
import com.alttd.shutdowninfo.config.Config;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ShutDown {

    public ShutDown(ProxyServer proxyServer) {
        Component ShutDownStart = MiniMessage.get().parse("<red>You have started the shutdown.</red>");
        Component ShutDownEnd = MiniMessage.get().parse("<green>You have ended the shutdown.</green>");

        LiteralCommandNode<CommandSource> commandStart = LiteralArgumentBuilder
                .<CommandSource>literal("shutdownstart")
                .requires(ctx -> ctx.hasPermission("command.proxy.shutdown"))
                .executes(context -> {
                    Config.setWhitelist(true);
                    context.getSource().sendMessage(ShutDownStart);

                    Component message = MiniMessage.get().parse(Config.KICK_MESSAGE);

                    ShutdownInfo.getPlugin().getProxy().getAllPlayers().stream()
                            .filter(player -> !player.hasPermission("shutdown.bypass"))
                            .forEach(player -> player.disconnect(message));

                    return 1;
                })
                .build();

        LiteralCommandNode<CommandSource> commandEnd = LiteralArgumentBuilder
                .<CommandSource>literal("shutdownend")
                .requires(ctx -> ctx.hasPermission("command.proxy.shutdown"))
                .executes(context -> {
                    Config.setWhitelist(false);
                    context.getSource().sendMessage(ShutDownEnd);
                    return 1;
                })
                .build();

        BrigadierCommand brigadierCommandStart = new BrigadierCommand(commandStart);
        BrigadierCommand brigadierCommandEnd = new BrigadierCommand(commandEnd);

        CommandMeta.Builder metaBuilderStart = proxyServer.getCommandManager().metaBuilder(brigadierCommandStart);
        CommandMeta.Builder metaBuilderEnd = proxyServer.getCommandManager().metaBuilder(brigadierCommandEnd);

        CommandMeta metaStart = metaBuilderStart.build();
        CommandMeta metaEnd = metaBuilderEnd.build();

        proxyServer.getCommandManager().register(metaStart, brigadierCommandStart);
        proxyServer.getCommandManager().register(metaEnd, brigadierCommandEnd);
    }
}
