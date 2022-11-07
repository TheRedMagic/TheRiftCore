package com.therift.theriftcore.Core;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.therift.theriftcore.TheRiftCore;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class GlobalChat implements PluginMessageListener  {

    private TheRiftCore main;

    public GlobalChat(TheRiftCore main){
        this.main = main;

    }

    public void SenderMessges(String message, Player player){

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward"); // So BungeeCord knows to forward it
        out.writeUTF("ALL");
        out.writeUTF("ChatM"); // The channel name to check if this your data

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            msgout.writeUTF(player.getWorld().getName()); // You can do anything you want with msgout
            if (Bukkit.getPlayer(player.getDisplayName()) != null){
                Player player2 = Bukkit.getPlayer(player.getDisplayName());
                User user = main.getApi().getPlayerAdapter(Player.class).getUser(player2);
                String prefix = user.getCachedData().getMetaData().getPrefix();

                String pre = ChatColor.translateAlternateColorCodes('&', prefix);
                msgout.writeUTF(pre);
            }
            msgout.writeUTF(player.getDisplayName());
            msgout.writeUTF(message);
            msgout.writeShort(123);
        } catch (IOException exception){
            exception.printStackTrace();
        }

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());

        player.sendPluginMessage(main, "BungeeCord", out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        System.out.println("Received something");
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("ChatM")){
            short len = in.readShort();
            byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);

            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            try {
                String ServerName = msgin.readUTF();
                String prefix = msgin.readUTF();
                String UserName = msgin.readUTF();
                String Message = msgin.readUTF();
                short somenuder = msgin.readShort();
                System.out.println("Message Recieavde");
                    for (Player player1 : Bukkit.getOnlinePlayers()){
                        player1.sendMessage(ChatColor.DARK_GRAY + "[" + ServerName + "] " + prefix + " " + UserName + ": " + ChatColor.GRAY + Message);
                    }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

}
