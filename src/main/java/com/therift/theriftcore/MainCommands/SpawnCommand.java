package com.therift.theriftcore.MainCommands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.therift.theriftcore.TheRiftCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor{
    private TheRiftCore main;
    public SpawnCommand(TheRiftCore main){
        this.main = main;
    }

    private Location spawnLocation;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            spawnLocation = new Location(player.getWorld(), main.getConfig().getInt("X"), main.getConfig().getInt("Y"), main.getConfig().getInt("Z"));
            if (!player.getWorld().getName().equals("Spawn")){

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF("Spawn");
                player.sendMessage(ChatColor.GREEN + "Connecting to spawn...");
                player.sendPluginMessage(main, "BungeeCord", out.toByteArray());
                player.teleport(spawnLocation);
                player.sendMessage(ChatColor.GREEN + "Teleported to spawn");

            }else {
                player.teleport(spawnLocation);
                player.sendMessage(ChatColor.GREEN + "Teleported to spawn");
            }
        }
        return false;
    }
}
