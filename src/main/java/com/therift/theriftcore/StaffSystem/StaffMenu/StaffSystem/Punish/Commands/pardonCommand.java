package com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem.Punish.Commands;

import com.therift.theriftcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class pardonCommand implements CommandExecutor {
    private Main main;
    public pardonCommand(Main main){
        this.main = main;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1){
            if (Bukkit.getOfflinePlayer(args[0]) != null) {
                main.getPlayerManager().setBan(Bukkit.getOfflinePlayer(args[0]).getUniqueId(), false);
                main.getPlayerManager().setActive(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString(), false);
                if (sender instanceof Player) {
                    ((Player) sender).sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                            ChatColor.RESET + ChatColor.GOLD + args[0] + " is Unbanned\n" +
                            ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――");
                } else {
                    System.out.println("―――――――――――――――――――――\n" +
                            args[0] + " is Unbanned\n" +
                            "―――――――――――――――――――――");
                }
            }
        }
        return false;
    }
}
