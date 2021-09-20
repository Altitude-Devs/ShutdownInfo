package com.alttd.shutdowninfo.events;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.Template;

import java.util.Collections;
import java.util.List;

public class WhitelistKickEvent {
    private final Player player;
    private String message;
    List<Template> templates;

    public WhitelistKickEvent(Player player, String message, List<Template> templates) {
        this.player = player;
        this.message = message;
        this.templates = templates;
    }

    public Player getPlayer() {return player;}

    public String getMessage() {return message;}

    public void appendMessage(String append) {message += append;}

    public List<Template> getTemplates()
    {
        return Collections.unmodifiableList(templates);
    }

    public void appendTemplates(Template template) {this.templates.add(template);}
//    public void appendTemplates(Template... templates) {this.templates.addAll(List.of(templates));}
}
