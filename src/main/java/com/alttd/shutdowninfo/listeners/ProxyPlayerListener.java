package com.alttd.shutdowninfo.listeners;

import com.alttd.shutdowninfo.ShutdownInfo;
import com.alttd.shutdowninfo.config.Config;
import com.alttd.shutdowninfo.events.WhitelistKickEvent;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ProxyPlayerListener {

    MiniMessage miniMessage;

    public ProxyPlayerListener()
    {
        miniMessage = MiniMessage.miniMessage();
    }

    @Subscribe
    public void playerLogin(LoginEvent event)
    {
        if (!Config.WHITELIST)
            return;

        Player player = event.getPlayer();

        if (event.getPlayer().hasPermission("shutdown.bypass"))
            return;

        TagResolver tagResolver = Placeholder.unparsed("player", player.getUsername());

        CompletableFuture<WhitelistKickEvent> whitelistKickEvent = ShutdownInfo.getPlugin().getProxy().getEventManager()
                .fire(new WhitelistKickEvent(player, Config.WHITELIST_MESSAGE, tagResolver));

        try {
            WhitelistKickEvent result = whitelistKickEvent.get();
            Component component = miniMessage.deserialize(result.getMessage(), result.getTagResolver());

            event.setResult(ResultedEvent.ComponentResult.denied(component));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
