package com.therift.theriftcore.Core;

import com.therift.theriftcore.TheRiftCore;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;

public class LuckPermListener {
    private TheRiftCore main;
    public LuckPermListener(TheRiftCore main, LuckPerms luckPerms){
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
