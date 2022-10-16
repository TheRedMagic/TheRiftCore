package com.therift.theriftcore.StaffSystem;

import com.therift.theriftcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AllPlayersCommand implements CommandExecutor {
    private Main main;
    public AllPlayersCommand(Main main){
        this.main = main;
    }
    private ResultSet OnlinePlayers;
    private UUID uuid;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("therift.allplayers")) {
                Player target;

                OnlinePlayers = main.getPlayerManager().getOnlineResultset();

                try {
                    player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                    while (OnlinePlayers.next()) {
                        uuid = UUID.fromString(OnlinePlayers.getString("UUID"));
                        if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
                            target = (Player) Bukkit.getOfflinePlayer(uuid);
                            player.sendMessage(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Displayname : " + target.getDisplayName() + "\n" + "UUID : " + uuid);
                            player.sendMessage("\n");
                        }

                    }
                    player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }
}
