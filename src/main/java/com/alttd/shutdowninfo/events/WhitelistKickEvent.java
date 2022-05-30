package com.alttd.shutdowninfo.events;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.*;

public class WhitelistKickEvent {
    private final Player player;
    private String message;
    private final TagResolver tagResolver;

    public WhitelistKickEvent(Player player, String message, TagResolver tagResolver) {
        this.player = player;
        this.message = message;
        this.tagResolver = tagResolver;
    }

    public Player getPlayer() {return player;}

    public String getMessage() {return message;}

    public void appendMessage(String append) {message += append;}

    public TagResolver getTagResolver()
    {
        return tagResolver;
    }

    public void appendTemplate(HashMap<String, String> templateMap) {
        for (Map.Entry<String, String> entry : templateMap.entrySet()) {
            TagResolver.resolver(tagResolver, Placeholder.unparsed(entry.getKey(), entry.getValue()));
        }
    }
}
