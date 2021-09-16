package com.alttd.shutdowninfo;

import com.alttd.shutdowninfo.commands.Reload;
import com.alttd.shutdowninfo.commands.ShutDown;
import com.alttd.shutdowninfo.config.Config;
import com.alttd.shutdowninfo.listeners.PingListener;
import com.alttd.shutdowninfo.listeners.ProxyPlayerListener;
import com.alttd.shutdowninfo.util.ALogger;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(id = "shutdowninfo", name = "ShutdownInfo", version = "1.0.0",
        description = "A plugin to show shutdown information.",
        authors = {"teri"}
)
public class ShutdownInfo {

    private static ShutdownInfo plugin;
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    @Inject
    public ShutdownInfo(ProxyServer proxyServer, Logger proxyLogger, @DataDirectory Path proxydataDirectory)
    {
        plugin = this;
        server = proxyServer;
        logger = proxyLogger;
        dataDirectory = proxydataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        ALogger.init(logger);
        ReloadConfig();
        server.getEventManager().register(this, new ProxyPlayerListener());
        server.getEventManager().register(this, new PingListener());
        loadCommands();
    }

    public void ReloadConfig() {
        Config.init();
        ALogger.info("Reloaded ShutdownInfo config.");
    }

    public void loadCommands() {// all (proxy)commands go here
        new Reload(server);
        new ShutDown(server);
    }

    public File getDataDirectory() {
        return dataDirectory.toFile();
    }

    public static ShutdownInfo getPlugin() {
        return plugin;
    }

    public Logger getLogger() {
        return logger;
    }

    public ProxyServer getProxy() {
        return server;
    }
}

