package com.therift.theriftcore.Core;

import com.therift.theriftcore.Discord.DiscordListener;
import com.therift.theriftcore.Discord.Commands.UserCommands.VerifyCommand;
import com.therift.theriftcore.Main;
import com.therift.theriftcore.StaffSystem.StaffMenu.ReportSystem.ReportManager;
import com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem.Punish.Commands.ChatMuteCommand;
import com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem.StaffMode.StaffManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.awt.*;
import java.util.HashMap;

public class CoreListener implements Listener{

    private static Main main;
    private GlobalChat globalChat;
    private HashMap<Player, String> rank = new HashMap<>();
    private TagTab tagTab;
    public CoreListener(Main main){
        this.main = main;
        globalChat = new GlobalChat(main);
        tagTab = new TagTab(main);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        main.getPlayerManager().setServer(e.getPlayer().getUniqueId(), e.getPlayer().getWorld().getName());
        TagTab.setNameTag(e.getPlayer());
        TagTab.newTag();
        TagTab.onJoin(e.getPlayer());
        rank.put(e.getPlayer(), main.getApi().getUserManager().getUser(e.getPlayer().getUniqueId()).getPrimaryGroup());
        new Tab(main).setTab();
    }

    @EventHandler
    public void onChat(PlayerChatEvent e) {
        e.setCancelled(true);
        Player player = e.getPlayer();
        Boolean badWord = false;
        boolean ismute = main.getPlayerManager().isMute(e.getPlayer().getUniqueId());
        if (!ismute) {
            if (!ChatMuteCommand.chatmuted || player.hasPermission("therift.staff")) {
                if (!StaffManager.inReason && !ReportManager.inReportReason) {

                    String RawMessage = e.getMessage();
                    String Message = RawMessage.replace(" ", "");

                    for (String s : main.getConfig().getStringList("Bad-word")) {
                        if (Message.toLowerCase().contains(s)) {
                            TextChannel textChannel = DiscordListener.jda.getGuildById(main.getConfig().getString("GuildID")).getTextChannelById(main.getConfig().getString("PunishLogId"));
                            EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Punish Log").setDescription("**" + e.getPlayer().getName() + "**" + "\nJust send a bad word in Minecraft" + "\n||" + e.getMessage() + "||");
                            textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
                            e.getPlayer().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                    ChatColor.RESET + ChatColor.RED + "Don't use that language in this server\n" +
                                    ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――");
                            badWord = true;
                            break;
                        }
                    }
                } else if (StaffManager.inReason) {
                    e.setCancelled(true);
                }
                if (!StaffManager.inReason && !ReportManager.inReportReason) {
                    if (!badWord) {
                        for (Player player1 : Bukkit.getOnlinePlayers()) {
                            if (Bukkit.getPlayer(player.getDisplayName()) != null) {
                                Player player2 = Bukkit.getPlayer(player.getDisplayName());
                                User user = main.getApi().getPlayerAdapter(Player.class).getUser(player2);
                                String prefix = user.getCachedData().getMetaData().getPrefix();

                                String pre = ChatColor.translateAlternateColorCodes('&', prefix);
                                player1.sendMessage(ChatColor.DARK_GRAY + "[" + e.getPlayer().getWorld().getName() + "] " + pre + " " + e.getPlayer().getDisplayName() + ": " + ChatColor.GRAY + e.getMessage());
                                globalChat.SenderMessges(e.getMessage(), e.getPlayer());

                                TextChannel textChannel2 =  DiscordListener.jda.getGuildById(Long.parseLong("997076075509194795")).getTextChannelById("1018857686428823623");
                                EmbedBuilder embedBuilder2 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle(e.getPlayer().getDisplayName()).setDescription("Sended : " + e.getMessage());
                                textChannel2.sendMessageEmbeds(embedBuilder2.build()).queue();


                            }
                        }
                    }
                }
            } else {
                player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                        ChatColor.RESET + ChatColor.RED + "Chat has been muted\n" +
                        ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――");
            }
        }else {
            player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                    ChatColor.RESET + ChatColor.RED + "You are muted\n" +
                    ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――");
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){

        main.getPlayerManager().setServer(event.getPlayer().getUniqueId(), "Null");
        TagTab.removeTag(event.getPlayer());
        if (rank.get(event.getPlayer()).equals(main.getApi().getUserManager().getUser(event.getPlayer().getUniqueId()).getPrimaryGroup())){
            main.getPlayerManager().setRank(event.getPlayer().getUniqueId(), main.getApi().getUserManager().getUser(event.getPlayer().getUniqueId()).getPrimaryGroup());
        }
        new Tab(main).setTab();
        TagTab.onQuit(event.getPlayer());
        if (rank.containsKey(event.getPlayer())) {
            rank.remove(event.getPlayer());
        }
        if (VerifyCommand.VerifyCode.containsKey(event.getPlayer().getDisplayName())){
            VerifyCommand.VerifyCode.remove(event.getPlayer().getDisplayName());
        }
    }
}
