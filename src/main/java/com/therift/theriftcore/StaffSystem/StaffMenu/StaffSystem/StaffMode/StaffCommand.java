package com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem.StaffMode;

import com.therift.theriftcore.TheRiftCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class StaffCommand implements CommandExecutor {
    private TheRiftCore main;
    public StaffCommand(TheRiftCore main){
        this.main = main;
        staffManager = new StaffManager(main);
    }
    private HashMap<UUID, Boolean> toggle = new HashMap<>();
    public static StaffManager staffManager;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){

            Player player = (Player) sender;
            if (toggle.containsKey(player.getUniqueId())) {
                if (toggle.get(player.getUniqueId())) {
                    staffManager.closeStaffMenu(player);
                    toggle.replace(player.getUniqueId(), false);
                } else if (!toggle.get(player.getUniqueId())) {
                    staffManager.openStaffMenu(player);
                    toggle.replace(player.getUniqueId(), true);
                } else {
                    player.sendMessage(ChatColor.RED + "Error");
                }
            } else {
                toggle.put(player.getUniqueId(), true);
                staffManager.openStaffMenu(player);
            }
        }
        return false;
    }
}
