package com.therift.theriftcore.StaffSystem.StaffMenu.ReportSystem;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.sun.jna.platform.win32.WinDef;
import com.therift.theriftcore.Discord.DiscordListener;
import com.therift.theriftcore.Main;
import com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem.PageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.io.BufferedReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class ReportManager {
    private Main main;
    private List<String> inReport = new ArrayList<>();
    private Boolean inReasonB = false;
    private Boolean inReasonP = false;
    private HashMap<Player, String> reason = new HashMap<>();
    private HashMap<Player, OfflinePlayer> target = new HashMap<>();
    public static Boolean inReportReason = false;
    public ReportManager(Main main){
        this.main = main;
    }

    //GUIS
    public void openChooseMenu(Player player){
        inReport.add(player.getUniqueId().toString());

        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Report");

        ItemStack player1 = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta playerMeta = player1.getItemMeta();
        playerMeta.setDisplayName(ChatColor.BLUE + "Report Player");
        player1.setItemMeta(playerMeta);

        ItemStack bug = new ItemStack(Material.BARRIER);
        ItemMeta bugMeta = bug.getItemMeta();
        bugMeta.setDisplayName(ChatColor.BLUE + "Report a Bug");
        bug.setItemMeta(bugMeta);

        inv.setItem(19, player1);
        inv.setItem(20, player1);
        inv.setItem(21, player1);
        inv.setItem(28, player1);
        inv.setItem(29, player1);
        inv.setItem(30, player1);
        inv.setItem(37, player1);
        inv.setItem(38, player1);
        inv.setItem(39, player1);
        inv.setItem(23, bug);
        inv.setItem(24, bug);
        inv.setItem(25, bug);
        inv.setItem(32, bug);
        inv.setItem(33, bug);
        inv.setItem(34, bug);
        inv.setItem(41, bug);
        inv.setItem(42, bug);
        inv.setItem(43, bug);

        player.openInventory(inv);
    }
    public void openBugGUI(Player player){

        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GREEN + "Confirm Report");

        ItemStack kick = new ItemStack(Material.BARRIER);
        ItemMeta kickMeta = kick.getItemMeta();
        kickMeta.setDisplayName(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Confirm Report");
        kickMeta.setLore(Arrays.asList(ChatColor.GRAY + "Bug explanation : " + reason.get(player)));
        kick.setItemMeta(kickMeta);

        inv.setItem(4, kick);
        inv.setItem(0, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(1, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(2, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(3, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(5, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(6, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(7, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(8, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        player.openInventory(inv);
    }
    public void openPlayerGUI(Player player){

        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GREEN + "Confirm Player Report");

        ItemStack kick = new ItemStack(Material.BARRIER);
        ItemMeta kickMeta = kick.getItemMeta();
        kickMeta.setDisplayName(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Confirm Player Report");
        ArrayList lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Reporting Player : " + target.get(player).getName());
        lore.add(ChatColor.GRAY + "Report Reason : " + reason.get(player));
        kickMeta.setLore(lore);
        kick.setItemMeta(kickMeta);

        inv.setItem(4, kick);
        inv.setItem(0, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(1, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(2, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(3, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(5, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(6, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(7, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(8, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        player.openInventory(inv);
    }
    private void playerList(Player player, int page){

        Inventory gui = Bukkit.createInventory(null, 54, "Online players - "+ page);

        List<ItemStack> allItems = new ArrayList<>();
        for (int i = 0; i< allItems.size(); i++){
            allItems.remove(i);
        }

        ResultSet players = main.getPlayerManager().getOnlineResultset();

        try {
            while (players.next()){
                String uuid = players.getString("uuid");
                String name = main.getPlayerManager().getPlayerUserName(uuid);

                ItemStack Skull = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) Skull.getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + name);
                meta.setOwner(name);
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Click to select player"));
                meta.setLocalizedName(name);
                Skull.setItemMeta(meta);
                allItems.add(Skull);

            }

            ItemStack left;

            ItemMeta leftMeta;
            if (PageUtil.isPageValid(allItems, page -1, 52)){
                left = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                leftMeta = left.getItemMeta();
                leftMeta.setDisplayName(ChatColor.GREEN + "Back page");
            } else{
                left = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                leftMeta = left.getItemMeta();
                leftMeta.setDisplayName(ChatColor.GREEN + "Can't go to back page");
            }
            leftMeta.setLocalizedName(page + "");
            left.setItemMeta(leftMeta);
            gui.setItem(45, left);

            ItemStack right;
            ItemMeta rightMeta;
            if (PageUtil.isPageValid(allItems, page +1, 52)){
                right = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                rightMeta = right.getItemMeta();
                rightMeta.setDisplayName(ChatColor.GREEN + "Next page");
            } else{
                right = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                rightMeta = right.getItemMeta();
                rightMeta.setDisplayName(ChatColor.GREEN + "Can't go to next page");
            }
            right.setItemMeta(rightMeta);
            gui.setItem(53, right);

            for (ItemStack is : PageUtil.getPageItems(allItems, page, 52)){
                gui.setItem(gui.firstEmpty(), is);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        player.openInventory(gui);

    }






    //Events
    public void onInvClick(InventoryClickEvent e){
        if (inReport.contains(e.getWhoClicked().getUniqueId().toString())){
            Player player = (Player) e.getWhoClicked();
            e.setCancelled(true);
            //Main GUI
            if (e.getView().getTitle().equals(ChatColor.BLUE + "Report") && e.getCurrentItem() != null){
                switch (e.getCurrentItem().getType()){
                    case PLAYER_HEAD:
                        playerList(player, 1);
                        break;
                    case BARRIER:
                        inReasonB = true;
                        inReportReason = true;
                        player.closeInventory();
                        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                ChatColor.RESET + ChatColor.GOLD + "Explain the bug" +
                                ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                        break;
                }
            }

            //Player List
            if (e.getView().getTitle().contains("Online players - ") && e.getCurrentItem() != null){
                if (e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)){
                    OfflinePlayer target = Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getLocalizedName());
                    inReasonP = true;
                    player.closeInventory();
                    inReportReason = true;
                    this.target.put(player, target);
                    player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                            ChatColor.RESET + ChatColor.GOLD + "Explain reason of player report" +
                            ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                }
            }

            //Bug GUI
            if (e.getView().getTitle().equals(ChatColor.DARK_GREEN + "Confirm Report") && e.getCurrentItem() != null){
                if (e.getCurrentItem().getType().equals(Material.BARRIER)){
                    if (main.getPlayerManager().isDiscordLinked(player.getUniqueId())){
                        createPrivateChannel(player, "-bug-report", main.getConfig().getString("ReportChannelID"), "Bug");
                        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                ChatColor.RESET + ChatColor.GOLD + "Bug have been report\nYou have been added to the discord channel " + player.getDisplayName() + "-bug-report" +
                                ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                    } else {
                        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                ChatColor.RESET + ChatColor.GOLD + "Bug has been reported" +
                                ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                    }
                    if (!main.getPlayerManager().getOnlineStaff().isEmpty()){
                        for (UUID uuid : main.getPlayerManager().getOnlineStaff()){
                            OfflinePlayer player1 = Bukkit.getOfflinePlayer(uuid);
                            String s = ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                    ChatColor.RESET + ChatColor.GOLD + "There is a new Bug Report" +
                                    ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――";
                            if (player1.isOnline()){
                                player1.getPlayer().sendMessage(s);
                            }else {
                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("Message");
                                out.writeUTF(player1.getName());
                                out.writeUTF(s);
                            }
                        }
                    }

                    TextChannel textChannel = DiscordListener.jda.getGuildById(main.getConfig().getString("GuildID")).getTextChannelById(main.getConfig().getString("ReportLogId"));
                    EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Report Log").setDescription("Reported By : " + player.getDisplayName()
                            + "\nType of Report : Bug" + "\nExplanation : " + reason.get(player));
                    textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
                    reason.remove(player);
                    player.closeInventory();
                    inReport.remove(player.getUniqueId().toString());
                }
            }

            //Player GUi
            if (e.getView().getTitle().equals(ChatColor.DARK_GREEN + "Confirm Player Report") && e.getCurrentItem() != null){
                if (e.getCurrentItem().getType().equals(Material.BARRIER)){
                    if (main.getPlayerManager().isDiscordLinked(player.getUniqueId())){
                        createPrivateChannel(player, "-player-report", main.getConfig().getString("ReportChannelID"), "Player");
                        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                ChatColor.RESET + ChatColor.GOLD + "Player have been report\nYou have been added to the discord channel " + player.getDisplayName() + "-player-report" +
                                ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                    }else {
                        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                ChatColor.RESET + ChatColor.GOLD +  target.get(player) + " has been reported" +
                                ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");

                    }

                    if (!main.getPlayerManager().getOnlineStaff().isEmpty()){
                        for (UUID uuid : main.getPlayerManager().getOnlineStaff()){
                            OfflinePlayer player1 = Bukkit.getOfflinePlayer(uuid);
                            String s = ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                    ChatColor.RESET + ChatColor.GOLD + "There is a new Player Report" +
                                    ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――";
                            if (player1.isOnline()){
                                player1.getPlayer().sendMessage(s);
                            }else {
                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("Message");
                                out.writeUTF(player1.getName());
                                out.writeUTF(s);
                                player.sendPluginMessage(main, "BungeeCord", out.toByteArray());
                            }
                        }
                    }
                    TextChannel textChannel = DiscordListener.jda.getGuildById(main.getConfig().getString("GuildID")).getTextChannelById(main.getConfig().getString("ReportLogId"));
                    EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Report Log").setDescription("Reported By : " + player.getDisplayName()
                            + "\nType of Report : Player" + "\nReported Player : " + target.get(player).getName() + "\nReason : " + reason.get(player));
                    textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
                    reason.remove(player);
                    target.remove(player);
                    player.closeInventory();
                    inReport.remove(player.getUniqueId().toString());
                }
            }
        }
    }
    public void onChat(PlayerChatEvent e){
        if (inReasonB){
            e.setCancelled(true);
            if (reason.containsKey(e.getPlayer())){
                reason.remove(e.getPlayer());
            }
            reason.put(e.getPlayer(), e.getMessage());
            inReasonB = false;
            openBugGUI(e.getPlayer());
            inReportReason = false;
        } else if (inReasonP){
            e.setCancelled(true);
            if (reason.containsKey(e.getPlayer())){
                reason.remove(e.getPlayer());
            }
            reason.put(e.getPlayer(), e.getMessage());
            inReasonP = false;
            openPlayerGUI(e.getPlayer());
            inReportReason = false;
        }
    }


    //Util
    private void createPrivateChannel(Player player, String Title, String ChannelID, String ReportType){
        Guild guild = DiscordListener.jda.getGuildById(main.getConfig().getString("GuildID"));
            guild.createTextChannel(player.getDisplayName() + Title).setParent(guild.getCategoryById(ChannelID))
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .addPermissionOverride(guild.getRoleById("1002216542504755240"), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), null)
                    .addMemberPermissionOverride(main.getPlayerManager().getDiscordID(player.getUniqueId()), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), null)
                    .queue();
        List<TextChannel> textChannelList = guild.getTextChannelsByName(player.getDisplayName() + Title, true);
        if (textChannelList.isEmpty()){
            System.out.println("b");
            for (TextChannel textChannel : textChannelList){
                System.out.println("a");
                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Report Info").setDescription("Reported By : " + player.getDisplayName()
                        + "\nType of Report : " + ReportType + "\nExplanation : " + reason.get(player));
                textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
                EmbedBuilder embedBuilder1 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Report Info").setDescription("Please provide more info about the report\n" + guild.getMemberById(main.getPlayerManager().getDiscordID(player.getUniqueId())).getAsMention());
                textChannel.sendMessageEmbeds(embedBuilder1.build()).queue();
            }
        }

    }
}
