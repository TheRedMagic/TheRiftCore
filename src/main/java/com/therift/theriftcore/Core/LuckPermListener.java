package com.therift.theriftcore.Core;

import com.therift.theriftcore.Main;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.event.user.UserLoadEvent;
import net.luckperms.api.event.user.track.UserDemoteEvent;
import net.luckperms.api.event.user.track.UserPromoteEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class LuckPermListener {
    private Main main;
    public LuckPermListener(Main main, LuckPerms luckPerms){
        this.main = main;

        EventBus eventBus = luckPerms.getEventBus();

        eventBus.subscribe(main, NodeAddEvent.class, this::onNoteAdd);
        eventBus.subscribe(main, NodeRemoveEvent.class, this::onNoteRemove);

    }

    private void onNoteAdd(NodeAddEvent e){
        TagTab.newTag();
    }
    private void onNoteRemove(NodeRemoveEvent e){
        TagTab.newTag();
    }
}
