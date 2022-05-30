package com.alttd.shutdowninfo.listeners;

import com.alttd.shutdowninfo.config.Config;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class PingListener {

    MiniMessage miniMessage;

    public PingListener() {
        miniMessage = MiniMessage.miniMessage();
    }

    @Subscribe
    public void onPing(ProxyPingEvent event) {
        if (!Config.WHITELIST)
            return;

        ServerPing.Builder builder = event.getPing().asBuilder();
        Component component = miniMessage.deserialize(Config.PING_MESSAGE);

        builder.onlinePlayers(-1);
        builder.maximumPlayers(0);
        builder.clearSamplePlayers();
        builder.description(component);

        event.setPing(builder.build());
    }
}
