package com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatMuteCommand implements CommandExecutor {

    public static boolean chatmuted = false;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            if (chatmuted){
                chatmuted = false;
                for (Player player : Bukkit.getOnlinePlayers()){
                    player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                            ChatColor.RESET + ChatColor.GREEN + "Chat has been unmuted" +
                            ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                }
            } else if (!chatmuted) {
                chatmuted = true;
                for (Player player : Bukkit.getOnlinePlayers()){
                    player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                            ChatColor.RESET + ChatColor.RED + "Chat has been muted" +
                            ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                }
            }
        }
        return false;
    }
}
