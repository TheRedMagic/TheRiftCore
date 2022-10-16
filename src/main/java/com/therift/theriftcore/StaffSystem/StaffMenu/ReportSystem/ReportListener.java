package com.therift.theriftcore.StaffSystem.StaffMenu.ReportSystem;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;

public class ReportListener implements Listener {
    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        if (ReportCommand.reportManager != null) {
            ReportCommand.reportManager.onInvClick(e);
        }
    }
    @EventHandler
    public void onChat(PlayerChatEvent e){
        if (ReportCommand.reportManager != null) {
            ReportCommand.reportManager.onChat(e);
        }
    }
}
