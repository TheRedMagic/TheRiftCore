package com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem;

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

public class WarmCommand implements CommandExecutor {
    private Main main;
    public WarmCommand(Main main){
        this.main = main;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length >= 2){

                if (Bukkit.getOfflinePlayer(args[0]) != null){
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                    StringBuilder stringBuilder = new StringBuilder(args[1]);
                    boolean a = false;
                    String last = null;
                    for (int i = 1; i < args.length; i++){
                        if (!a) {
                            a =true;

                        } else if (a) {
                            stringBuilder.append(args[i]).append(" ");
                        }

                        last = args[i];
                    }
                    String reason = stringBuilder.toString();

                    main.getPlayerManager().addToPunishLog(target.getUniqueId().toString(), target.getName(), "Warn", reason, player.getDisplayName(), false);
                    if (target.isOnline()){
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Message");
                        out.writeUTF(target.getName());
                        out.writeUTF(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                ChatColor.RESET + ChatColor.RED + "You have been warn\nReason : " + reason +
                                ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                    }

                    player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                            ChatColor.RESET + ChatColor.RED + "You Warned " + target.getName() +
                            ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");


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
