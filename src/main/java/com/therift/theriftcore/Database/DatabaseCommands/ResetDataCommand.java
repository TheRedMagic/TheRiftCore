package com.therift.theriftcore.Database.DatabaseCommands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.therift.theriftcore.TheRiftCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResetDataCommand implements CommandExecutor, PluginMessageListener {
    private TheRiftCore main;
    public ResetDataCommand(TheRiftCore main){
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

                            PreparedStatement ps1 = main.getDatabase().getConnection().prepareStatement("DELETE FROM PunishData WHERE UUID = ?");
                            ps1.setString(1, target.getUniqueId().toString());
                            ps1.executeUpdate();

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        ((Player) sender).sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" +
                                "\n" + ChatColor.GREEN +ChatColor.BOLD + target.getDisplayName() + "has been reset" +
                                ChatColor.GRAY + ChatColor.BOLD + "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" );

                    }else if (Bukkit.getOfflinePlayer(args[0]) != null){
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                        if (main.getPlayerManager() .isPlayerOnline(offlinePlayer.getUniqueId())) {
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("KickPlayer");
                            out.writeUTF(offlinePlayer.getName());
                            out.writeUTF(ChatColor.RED + "Your player data has been reset");
                            ((Player) sender).sendPluginMessage(main, "BungeeCord", out.toByteArray());
                        }
                        try {
                            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("DELETE FROM PlayerData WHERE UUID = ?");
                            ps.setString(1, String.valueOf(offlinePlayer.getUniqueId()));
                            ps.executeUpdate();

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        ((Player) sender).sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" +
                                "\n" + ChatColor.GREEN +ChatColor.BOLD + offlinePlayer.getName() + "has been reset" +
                                ChatColor.GRAY + ChatColor.BOLD + "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" );

                    }

                    else {
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

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {

    }
}
