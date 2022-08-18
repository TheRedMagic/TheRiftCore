package com.therift.theriftcore.Core;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.therift.theriftcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;

public class GlobalChat  {

    private Main main;

    public GlobalChat(Main main){
        this.main = main;

    }

    public void SenderMessges(String message, Player player){

        System.out.println("Sending Message");
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ALL");
        out.writeUTF("Message");


        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);

        try {
            msgout.writeUTF(message);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());

        player.getServer().sendPluginMessage(main, "BungeeCord", out.toByteArray());
        System.out.println("Message send");

    }

    public void onMessageReceived(String channel, Player player, byte[] message) {
        System.out.println("Received something");
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("Message")){


            short len = in.readShort();
            byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);

            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            String message1;

            System.out.println("Message found");

            try {
                message1 = msgin.readUTF();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println(message1);

        }

    }
}
