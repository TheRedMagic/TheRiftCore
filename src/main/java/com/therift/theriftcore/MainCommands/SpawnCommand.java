package com.therift.theriftcore.MainCommands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.therift.theriftcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor{
    private Main main;
    public SpawnCommand(Main main){
        this.main = main;
    }

    private Location spawnLocation;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            spawnLocation = new Location(player.getWorld(), main.getConfig().getInt("X"), main.getConfig().getInt("Y"), main.getConfig().getInt("Z"));
            if (player.getWorld() == Bukkit.getWorld("Spawn")){

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF("Spawn");
                player.sendMessage(ChatColor.GREEN + "Connecting to spawn...");
                player.sendPluginMessage(main, "BungeeCord", out.toByteArray());
                player.teleport(spawnLocation);

            }else {
                player.teleport(spawnLocation);
            }
        }
        return false;
    }
}