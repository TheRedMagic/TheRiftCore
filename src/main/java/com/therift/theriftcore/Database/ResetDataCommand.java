package com.therift.theriftcore.Database;

import com.therift.theriftcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResetDataCommand implements CommandExecutor {
    private Main main;
    public ResetDataCommand(Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            if (((Player) sender).hasPermission("therift.resetdata")) {
                if (args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        Player target = (Player) Bukkit.getPlayer(args[0]);
                            target.kickPlayer(ChatColor.RED + "Your player data has been reset");
                        try {
                            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("DELETE FROM PlayerData WHERE UUID = ?");
                            ps.setString(1, String.valueOf(target.getUniqueId()));
                            ps.executeUpdate();

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        ((Player) sender).sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" +
                                "\n" + ChatColor.GREEN +ChatColor.BOLD + target.getDisplayName() + "has been reset" +
                                ChatColor.GRAY + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" );

                    } else {
                        ((Player) sender).sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" +
                                "\n" + ChatColor.RED +ChatColor.BOLD + "Can't find player\n" +
                                ChatColor.GRAY + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" );
                    }
                } else {
                    ((Player) sender).sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" +
                            "\n" + ChatColor.RED +ChatColor.BOLD + "Invalid usage! '/reset <Player>'" +
                            ChatColor.GRAY + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" );

                }
            }else {
                ((Player) sender).sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" +
                        "\n" + ChatColor.RED +ChatColor.BOLD + "You don't have permission to use the command\n" +
                        ChatColor.GRAY + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" );


            }
        }
        return false;
    }
}
