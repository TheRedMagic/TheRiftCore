package com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem;

import com.therift.theriftcore.Main;
import com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem.StaffCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class StaffListener implements Listener {
    private Main main;
    public StaffListener(Main main){
        this.main = main;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (StaffCommand.staffManager != null) {
            StaffCommand.staffManager.onClick(e);
        }
    }
    @EventHandler
    public void onInt(PlayerInteractEvent e){
        if (StaffCommand.staffManager != null) {
            StaffCommand.staffManager.onInt(e);
            StaffCommand.staffManager.onIntF(e);
        }
    }
    @EventHandler
    public void onEnityClick(PlayerInteractEntityEvent e){
        if (StaffCommand.staffManager != null) {
            StaffCommand.staffManager.onEnityClick(e);
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if (StaffCommand.staffManager != null) {
            StaffCommand.staffManager.onJoin(e.getPlayer());
            if (e.getPlayer().hasPermission("therift.staff")){
                if (main.getPlayerManager().inStaff(e.getPlayer().getUniqueId())){
                    StaffCommand.staffManager.openStaffMenu(e.getPlayer());
                    if (main.getPlayerManager().inVanish(e.getPlayer().getUniqueId())){
                        e.setJoinMessage(null);
                        StaffCommand.staffManager.vanish = false;
                        StaffCommand.staffManager.setVanish(e.getPlayer());
                    }
                }

            }
            StaffCommand.staffManager.updateVanish(e);
        }
    }
    @EventHandler void onDrop(PlayerDropItemEvent e){
        if (StaffCommand.staffManager != null) {
            StaffCommand.staffManager.onDrop(e);
        }
    }
    @EventHandler void onChat(PlayerChatEvent e){
        if (StaffCommand.staffManager != null) {
        StaffCommand.staffManager.onChat(e);}
    }
    @EventHandler void onMove(PlayerMoveEvent e){
        if (StaffCommand.staffManager != null){
            StaffCommand.staffManager.onMove(e);
        }
    }
    @EventHandler void onPickUp(PlayerPickupItemEvent e){
        if (StaffCommand.staffManager != null){
            StaffCommand.staffManager.onPickup(e);
        }
    }
    @EventHandler void onQuit(PlayerQuitEvent e){
        StaffCommand.staffManager.onQuit(e.getPlayer());
    }
}
