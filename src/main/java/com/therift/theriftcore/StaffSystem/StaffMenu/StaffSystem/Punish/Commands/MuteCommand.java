package com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem.Punish.Commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.therift.theriftcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MuteCommand implements CommandExecutor {

    private Main main;
    public MuteCommand(Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player)  sender;
            if (args.length == 1){
                if (Bukkit.getOfflinePlayer(args[0]) != null){
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                    boolean ismute = main.getPlayerManager().isMute(target.getUniqueId());

                    if (ismute){
                        main.getPlayerManager().setMute(target.getUniqueId(), false);

                        if (main.getPlayerManager().isPlayerOnline(target.getUniqueId())){
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("Message");
                            out.writeUTF(target.getName());
                            out.writeUTF(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                    ChatColor.RESET + ChatColor.GREEN + "You have been unmuted" +
                                    ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                            player.sendPluginMessage(main, "BungeeCord", out.toByteArray());


                        }
                        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                ChatColor.RESET + ChatColor.GOLD + "Player has been unmuted" +
                                ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");

                    }else if (!ismute){
                        main.getPlayerManager().setMute(target.getUniqueId(), true);

                        if (main.getPlayerManager().isPlayerOnline(target.getUniqueId())){
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("Message");
                            out.writeUTF(target.getName());
                            out.writeUTF(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                    ChatColor.RESET + ChatColor.RED + "You have been muted" +
                                    ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                            player.sendPluginMessage(main, "BungeeCord", out.toByteArray());


                        }
                        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                ChatColor.RESET + ChatColor.GOLD + "Player has been muted" +
                                ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                    }



                }else {
                    player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                            ChatColor.RESET + ChatColor.RED + "Can't find player" +
                            ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                }
            }else {
                player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                        ChatColor.RESET + ChatColor.RED + "Invalid usage" +
                        ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
            }
        }
        return false;
    }
}
