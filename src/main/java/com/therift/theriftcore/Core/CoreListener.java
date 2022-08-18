package com.therift.theriftcore.Core;

import com.therift.theriftcore.Discord.VerifyCommand;
import com.therift.theriftcore.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class CoreListener implements Listener, PluginMessageListener {

    private Main main;
    private GlobalChat globalChat;
    public CoreListener(Main main){
        this.main = main;
        globalChat = new GlobalChat(main);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e){

    }

    @EventHandler
    public void onChat(PlayerChatEvent e){
        globalChat.SenderMessges(e.getMessage(), e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        if (VerifyCommand.VerifyCode.containsKey(event.getPlayer().getDisplayName())){
            VerifyCommand.VerifyCode.remove(event.getPlayer().getDisplayName());
        }
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        globalChat.onMessageReceived(channel, player, message);
    }
}
