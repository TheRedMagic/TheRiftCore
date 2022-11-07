package com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem.StaffMode;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.therift.theriftcore.Core.TagTab;
import com.therift.theriftcore.TheRiftCore;
import com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem.PageUtil;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.EulerAngle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StaffManager {
    private TheRiftCore main;
    private List<UUID> inMenu = new ArrayList<>();
    private List<UUID> inMenuList = new ArrayList<>();
    public static Boolean vanish = false;
    private ItemStack isVanished = new ItemStack(Material.LIME_DYE);
    public static List<UUID> Vanished = new ArrayList<>();
    private List<UUID> inReasonK = new ArrayList<>();
    private List<UUID> inReasonB = new ArrayList<>();
    private List<UUID> inReasonW = new ArrayList<>();
    private List<UUID> freeze = new ArrayList<>();
    public static boolean inReason = false;
    private HashMap<UUID, String> reason = new HashMap<>();
    private HashMap<UUID, Boolean> flight = new HashMap<>();
    private HashMap<UUID, OfflinePlayer> target = new HashMap<>();
    private HashMap<UUID, String > kickName = new HashMap<>();



    public StaffManager(TheRiftCore main){
        this.main = main;
    }
    public void openStaffMenu(Player player){
        player.sendMessage(ChatColor.GREEN + "You are now is staff mode");

        ItemMeta isVanishedMeta = isVanished.getItemMeta();
        isVanishedMeta.setDisplayName(ChatColor.GREEN + "Vanish");
        isVanishedMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to vanish"));
        isVanishedMeta.setLocalizedName("vanish");
        isVanished.setItemMeta(isVanishedMeta);

        ItemStack fly = new ItemStack(Material.FEATHER);
        ItemMeta flyMeta = fly.getItemMeta();
        flyMeta.setDisplayName(ChatColor.AQUA + "Toggle Flight");
        flyMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to toggle flight"));
        flyMeta.setLocalizedName("fly");
        fly.setItemMeta(flyMeta);

        ItemStack PlayerInfo = new ItemStack(Material.PAPER);
        ItemMeta playerInfoMeta = PlayerInfo.getItemMeta();
        playerInfoMeta.setDisplayName(ChatColor.BLUE + "Player Info");
        playerInfoMeta.setLocalizedName("playerinfo");
        playerInfoMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click on player to get there info"));
        PlayerInfo.setItemMeta(playerInfoMeta);

        ItemStack playerList = new ItemStack(Material.BOOK);
        ItemMeta playerListMeta = playerList.getItemMeta();
        playerListMeta.setDisplayName(ChatColor.DARK_AQUA + "Player List");
        playerListMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to get the player list"));
        playerListMeta.setLocalizedName("playerlist");
        playerList.setItemMeta(playerListMeta);

        ItemStack freezeStick = new ItemStack(Material.STICK);
        ItemMeta freezeStickMeta = playerList.getItemMeta();
        freezeStickMeta.setDisplayName(ChatColor.DARK_AQUA + "Freeze Player");
        freezeStickMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to freeze player"));
        freezeStickMeta.setLocalizedName("freezeplayer");
        freezeStick.setItemMeta(freezeStickMeta);

        player.getInventory().setItem(8, freezeStick);
        player.getInventory().setItem(4, playerList);
        player.getInventory().setItem(3, PlayerInfo);
        player.getInventory().setItem(1, fly);
        player.getInventory().setItem(0, isVanished);
        inMenu.add(player.getUniqueId());
        main.getPlayerManager().setInStaff(player.getUniqueId(), true);
        if (!inMenuList.contains(player.getUniqueId())) {
            inMenuList.add(player.getUniqueId());
        }
    }
    public void closeStaffMenu(Player player){
        player.sendMessage(ChatColor.RED + "You are now out of staff mode");
        if (!vanish) {
            vanish = true;
            setVanish(player);
        }
        ItemStack air = new ItemStack(Material.AIR);
        for (int i = 0; i < 9; i++){
            player.getInventory().setItem(i, air);
        }
        inMenu.remove(player.getUniqueId());
        main.getPlayerManager().setInStaff(player.getUniqueId(), false);
        if (inMenuList.contains(player.getUniqueId())) {
            inMenuList.remove(player.getUniqueId());
        }
    }
    public void onClick(InventoryClickEvent e){
        if (inMenu.contains(e.getWhoClicked().getUniqueId())){
            if (inMenuList.contains(e.getWhoClicked().getUniqueId())){
                e.setCancelled(true);

                //Player list
                if (e.getView().getTitle().contains("Players - ") && e.getCurrentItem() != null){
                    if (e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)){
                        Player player = (Player) e.getWhoClicked();
                        OfflinePlayer target = Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getLocalizedName());
                        playerInfo(player, target);
                    } else if (e.getCurrentItem().getItemMeta().getLocalizedName().equals("allplayers")) {
                        allPlayerList((Player) e.getWhoClicked(), 1);
                    }
                }

                //Player Info
                if (e.getView().getTitle().equals(ChatColor.BLUE.toString() + ChatColor.BOLD + "Player Info") && e.getCurrentItem() != null){
                    if (e.getCurrentItem().getType().equals(Material.ANVIL)){
                        playerPunish((Player) e.getWhoClicked(), Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getLocalizedName()));
                    } else if (e.getCurrentItem().getType().equals(Material.COMPASS)) {
                        String server = main.getPlayerManager().getServer(e.getCurrentItem().getItemMeta().getLocalizedName());
                        if (server != e.getWhoClicked().getWorld().getName()) {
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("Connect");
                            out.writeUTF(server);
                            Player player = (Player) e.getWhoClicked();
                            player.sendPluginMessage(main, "BungeeCord", out.toByteArray());
                        }else {
                            e.getWhoClicked().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                    ChatColor.RESET + ChatColor.GOLD + "You are in the same server" +
                                    ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                            e.getWhoClicked().closeInventory();
                        }
                    } else if (e.getCurrentItem().getType().equals(Material.ENDER_PEARL)) {
                        if (Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getLocalizedName()) != null){
                            e.getWhoClicked().teleport(Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getLocalizedName()).getLocation());
                            e.getWhoClicked().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                    ChatColor.RESET + ChatColor.GOLD + "You have teleported to " + e.getCurrentItem().getItemMeta().getLocalizedName() +
                                    ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                            e.getWhoClicked().closeInventory();
                        }else {
                            e.getWhoClicked().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                    ChatColor.RESET + ChatColor.GOLD + "You are not in the same server" +
                                    ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                            e.getWhoClicked().closeInventory();
                        }
                    }
                }


                //Punish Menu
                if(e.getView().getTitle().equals(ChatColor.RED.toString() + ChatColor.BOLD + "Punish Player") && e.getCurrentItem() != null){

                    //kick
                    String s = ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                            ChatColor.RESET + ChatColor.GOLD + "Type in reason\n" +
                            ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――";
                    if (e.getCurrentItem().getType().equals(Material.FEATHER)){
                        Player player = (Player) e.getWhoClicked();
                        inReasonK.add(player.getUniqueId());
                        player.sendMessage(s);
                        inReason = true;
                        target.put(player.getUniqueId(), Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getLocalizedName()));
                        kickName.put(player.getUniqueId(), e.getCurrentItem().getItemMeta().getLocalizedName());
                        e.getWhoClicked().closeInventory();
                    }
                    //ban
                    if (e.getCurrentItem().getType().equals(Material.ANVIL)){
                        Player player = (Player) e.getWhoClicked();
                        inReasonB.add(player.getUniqueId());
                        player.sendMessage(s);
                        inReason = true;
                        target.put(player.getUniqueId(), Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getLocalizedName()));
                        e.getWhoClicked().closeInventory();
                    }
                    //Warn
                    if (e.getCurrentItem().getType().equals(Material.RED_TERRACOTTA)){
                        Player player = (Player) e.getWhoClicked();
                        inReasonW.add(player.getUniqueId());
                        player.sendMessage(s);
                        inReason = true;
                        target.put(player.getUniqueId(), Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getLocalizedName()));
                        e.getWhoClicked().closeInventory();
                    }
                    //Mute
                    if (e.getCurrentItem().getType().equals(Material.BARRIER)){
                        Player player = (Player) e.getWhoClicked();
                        OfflinePlayer target = Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getLocalizedName());

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

                        player.closeInventory();
                    }

                }

                //Kick
                if (e.getView().getTitle().equals(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Confirm Kick") && e.getCurrentItem() != null){
                        if (e.getCurrentItem().getType().equals(Material.FEATHER)){
                            if (!main.getPlayerManager().isActiveBanned(target.get(e.getWhoClicked().getUniqueId()).getUniqueId().toString())){
                            main.getPlayerManager().addToPunishLog(target.get(e.getWhoClicked().getUniqueId()).getUniqueId().toString(), kickName.get(e.getWhoClicked().getUniqueId()), "Kick", reason.get(e.getWhoClicked().getUniqueId()), e.getWhoClicked().getName(), false);
                            kickName.remove(e.getWhoClicked().getUniqueId());
                            if (Bukkit.getPlayer(target.get(e.getWhoClicked().getUniqueId()).getName()) != null){
                                    Bukkit.getPlayer(target.get(e.getWhoClicked().getUniqueId()).getName()).kickPlayer(ChatColor.RED + "You just got kicked\nReason : " + reason.get(e.getWhoClicked().getUniqueId()));
                                    e.getWhoClicked().closeInventory();
                                     e.getWhoClicked().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" + ChatColor.RESET + ChatColor.GOLD + target.get((Player) e.getWhoClicked()).getName() + " was kicked\n" +
                                     ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――");
                                     e.getWhoClicked().closeInventory();
                     }else {
                         if (main.getPlayerManager().isPlayerOnline(target.get(e.getWhoClicked().getUniqueId()).getUniqueId())) {
                             ByteArrayDataOutput out = ByteStreams.newDataOutput();
                             out.writeUTF("KickPlayer");
                             out.writeUTF(ChatColor.RED + "You just got kicked\nReason : " + target.get(e.getWhoClicked().getUniqueId()).getName());
                             out.writeUTF(reason.get(e.getWhoClicked().getUniqueId()));
                             ((Player) e.getWhoClicked()).sendPluginMessage(main, "BungeeCord", out.toByteArray());
                             e.getWhoClicked().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                     ChatColor.RESET + ChatColor.GOLD + target.get(e.getWhoClicked().getUniqueId()).getName() + " was kicked\n" +
                                     ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――");
                             e.getWhoClicked().closeInventory();
                         } else {
                             e.getWhoClicked().sendMessage(ChatColor.RED + "Player needs to be online to kick");
                             e.getWhoClicked().closeInventory();
                         }
                     }
                         e.getWhoClicked().closeInventory();

                     }else {
                                e.getWhoClicked().closeInventory();
                                e.getWhoClicked().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                        ChatColor.RESET + ChatColor.GOLD + target.get(e.getWhoClicked().getUniqueId()).getName() + " is Banned\n" +
                                        ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――");
                            }
                 }
                }

                //Ban
                if (e.getView().getTitle().equals(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Confirm Ban") && e.getCurrentItem() != null){
                    if (e.getCurrentItem().getType().equals(Material.ANVIL)) {
                        if (!main.getPlayerManager().isActiveBanned(target.get(e.getWhoClicked().getUniqueId()).getUniqueId().toString())) {
                            main.getPlayerManager().setBan(target.get(e.getWhoClicked().getUniqueId()).getUniqueId(), true);
                            main.getPlayerManager().addToPunishLog(target.get(e.getWhoClicked().getUniqueId()).getUniqueId().toString(), target.get((e.getWhoClicked().getUniqueId())).getName(), "Ban", reason.get(e.getWhoClicked().getUniqueId()), e.getWhoClicked().getName(), true);
                            if (Bukkit.getPlayer(String.valueOf(target.get(e.getWhoClicked().getUniqueId()))) != null) {
                                Bukkit.getPlayer(String.valueOf(target.get(e.getWhoClicked().getUniqueId()))).kickPlayer(ChatColor.RED + "You are Banned\nReason : " + reason.get(e.getWhoClicked().getUniqueId()));
                                e.getWhoClicked().closeInventory();
                                e.getWhoClicked().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                        ChatColor.RESET + ChatColor.GOLD + target.get(e.getWhoClicked().getUniqueId()).getName() + " was Banned\n" +
                                        ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――");
                            } else if (main.getPlayerManager().isPlayerOnline(target.get(e.getWhoClicked().getUniqueId()).getUniqueId())) {
                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("KickPlayer");
                                out.writeUTF(target.get(e.getWhoClicked().getUniqueId()).getName());
                                out.writeUTF(ChatColor.RED + "You have been banned\nReason : " + reason.get(e.getWhoClicked()));
                                ((Player) e.getWhoClicked()).sendPluginMessage(main, "BungeeCord", out.toByteArray());
                                e.getWhoClicked().closeInventory();
                                e.getWhoClicked().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                        ChatColor.RESET + ChatColor.GOLD + target.get(e.getWhoClicked().getUniqueId()).getName() + " was Banned\n" +
                                        ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――");
                            } else {
                                e.getWhoClicked().closeInventory();
                                e.getWhoClicked().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                        ChatColor.RESET + ChatColor.GOLD + target.get(e.getWhoClicked().getUniqueId()).getName() + " was Banned\n" +
                                        ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――");
                            }
                        } else {
                            e.getWhoClicked().closeInventory();
                            e.getWhoClicked().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                    ChatColor.RESET + ChatColor.GOLD + target.get(e.getWhoClicked().getUniqueId()).getName() + " is already Banned" +
                                    ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――");
                        }
                        reason.remove(e.getWhoClicked().getUniqueId());
                        target.remove(e.getWhoClicked().getUniqueId());
                    }
                }

                //Warn
                if (e.getView().getTitle().equals(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Confirm Warn") && e.getCurrentItem() != null){
                    if (e.getCurrentItem().getType().equals(Material.RED_TERRACOTTA)) {
                        Player player = (Player) e.getWhoClicked();
                        if (!main.getPlayerManager().isActiveBanned(target.get(e.getWhoClicked().getUniqueId()).getUniqueId().toString())) {
                            if (Bukkit.getPlayer(target.get(e.getWhoClicked().getUniqueId()).getName()) != null) {
                                Bukkit.getPlayer(target.get(e.getWhoClicked().getUniqueId()).getName()).sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                        ChatColor.RESET + ChatColor.GOLD + "You have been warned for " + reason.get(player.getUniqueId()) + "\n" +
                                        ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――");
                                player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                        ChatColor.RESET + ChatColor.GOLD + "You have warned " + target.get(e.getWhoClicked().getUniqueId()).getName() + " for "  + reason.get(player.getUniqueId()) + "\n" +
                                        ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――");
                            } else if (main.getPlayerManager().isPlayerOnline(target.get(e.getWhoClicked().getUniqueId()).getUniqueId())) {
                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("Message");
                                out.writeUTF(target.get(e.getWhoClicked().getUniqueId()).getName());
                                out.writeUTF(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                        ChatColor.RESET + ChatColor.GOLD + "You have been warned for " + reason.get(player.getUniqueId()) + "\n" +
                                        ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――");
                                player.sendPluginMessage(main, "BungeeCord", out.toByteArray());

                                player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                        ChatColor.RESET + ChatColor.GOLD + "You have warned " + target.get(e.getWhoClicked().getUniqueId()).getName() + " for "  + reason.get(player.getUniqueId()) + "\n" +
                                        ChatColor.GRAY + ChatColor.BOLD + "―――――――――――――――――――――");
                            }
                            main.getPlayerManager().addWarnings(target.get(e.getWhoClicked().getUniqueId()).getUniqueId());
                            main.getPlayerManager().addToPunishLog(target.get(e.getWhoClicked().getUniqueId()).getUniqueId().toString(), target.get(e.getWhoClicked().getUniqueId()).getName(), "Warn", reason.get(player.getUniqueId()), e.getWhoClicked().getName(), false);
                            player.closeInventory();
                        }
                    }
                }
            }
        }
    }
    public void onChat(PlayerChatEvent e){
        if (inMenuList.contains(e.getPlayer().getUniqueId())){
            if (inReasonK.contains(e.getPlayer().getUniqueId())){
                e.setCancelled(true);
                if (reason.containsKey(e.getPlayer().getUniqueId())){
                    reason.remove(e.getPlayer().getUniqueId());
                }
                reason.put(e.getPlayer().getUniqueId(), e.getMessage());
                inReasonK.remove(e.getPlayer().getUniqueId());
                kickCon(e.getPlayer(), target.get(e.getPlayer().getUniqueId()));
                inReason = false;
            } else if (inReasonB.contains(e.getPlayer().getUniqueId())) {
                e.setCancelled(true);
                if (reason.containsKey(e.getPlayer().getUniqueId())){
                    reason.remove(e.getPlayer().getUniqueId());
                }
                reason.put(e.getPlayer().getUniqueId(), e.getMessage());
                inReasonB.remove(e.getPlayer().getUniqueId());
                banCon(e.getPlayer(), target.get(e.getPlayer().getUniqueId()));
                inReason = false;
            } else if (inReasonW.contains(e.getPlayer().getUniqueId())) {
                Player player = e.getPlayer();
                e.setCancelled(true);
                if (reason.containsKey(e.getPlayer().getUniqueId())){
                    reason.remove(e.getPlayer().getUniqueId());
                }
                inReasonK.remove(e.getPlayer().getUniqueId());
                reason.put(e.getPlayer().getUniqueId(), e.getMessage());
                WarnCon(player, target.get(e.getPlayer().getUniqueId()));
                inReason = false;
            }
        }
    }
    public void onInt(PlayerInteractEvent e){
        if (inMenu.contains(e.getPlayer().getUniqueId())){
            if (inMenuList.contains(e.getPlayer().getUniqueId())){
                if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                    if (e.getHand().equals(EquipmentSlot.HAND)){
                        if (e.getItem() != null) {
                            switch (e.getItem().getItemMeta().getLocalizedName()) {
                                case "vanish":
                                    setVanish(e.getPlayer());
                                    break;
                                case "playerlist":
                                    playerList(e.getPlayer(), 1);
                                    break;
                                case "fly":
                                    toggleFlight(e.getPlayer());
                                    break;
                            }
                        }else {
                            System.out.println("Not holding item");
                        }
                    }
                }
            }
        }
        if (freeze.contains(e.getPlayer().getUniqueId())){
            e.setCancelled(true);
        }
    }
    public void onQuit(Player player){
        if (player.hasPermission("therift.staff")) {
            if (inMenu.contains(player.getUniqueId())) {
                inMenu.remove(player.getUniqueId());
            }
            if (inMenuList.contains(player.getUniqueId())) {
                inMenuList.remove(player.getUniqueId());
            }
            if (Vanished.contains(player.getUniqueId())) {
                Vanished.remove(player.getUniqueId());
            }
            if (inReasonK.contains(player.getUniqueId())) {
                inReasonK.remove(player.getUniqueId());
            }
            if (inReasonB.contains(player.getUniqueId())) {
                inReasonB.remove(player.getUniqueId());
            }
            if (inReasonW.contains(player.getUniqueId())) {
                inReasonW.remove(player.getUniqueId());
            }
            if (reason.containsKey(player.getUniqueId())) {
                reason.remove(player.getUniqueId());
            }
            if (flight.containsKey(player.getUniqueId())) {
                flight.remove(player.getUniqueId());
            }
            if (target.containsKey(player.getUniqueId())) {
                target.remove(player.getUniqueId());
            }
            if (kickName.containsKey(player.getUniqueId())){
                kickName.remove(player.getUniqueId());
            }
        }
    }
    public void onJoin(Player player){
        if (player.hasPermission("therift.staff")){
            if (main.getPlayerManager().inStaff(player.getUniqueId())){
                UUID uuid = player.getUniqueId();
                inMenu.add(uuid);
                inMenuList.add(uuid);
            }
        }
    }
    private void toggleFlight(Player player){
        if (!flight.containsKey(player.getUniqueId())){
            flight.put(player.getUniqueId(), false);
        }

        if (flight.get(player.getUniqueId())){
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage(ChatColor.RED + "Flight is disable");
            flight.put(player.getUniqueId(), false);
        } else if (!flight.get(player.getUniqueId())){
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage(ChatColor.GREEN + "Flight is enable");
            flight.put(player.getUniqueId(), true);
        }
    }
    private void kickCon(Player player, OfflinePlayer target){
        Inventory invKick = Bukkit.createInventory(null, 9, ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Confirm Kick");

        ItemStack kick = new ItemStack(Material.FEATHER);
        ItemMeta kickMeta = kick.getItemMeta();
        kickMeta.setDisplayName(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Confirm Kick");
        kickMeta.setLore(Arrays.asList(ChatColor.GRAY + "Kick " + target.getName() + " for " + reason.get(player.getUniqueId())));
        kick.setItemMeta(kickMeta);

        invKick.setItem(4, kick);
        invKick.setItem(0, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(1, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(2, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(3, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(5, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(6, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(7, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(8, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        player.openInventory(invKick);
    }
    private void banCon(Player player, OfflinePlayer target){
        Inventory invKick = Bukkit.createInventory(null, 9, ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Confirm Ban");

        ItemStack kick = new ItemStack(Material.ANVIL);
        ItemMeta kickMeta = kick.getItemMeta();
        kickMeta.setDisplayName(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Confirm Ban");
        kickMeta.setLore(Arrays.asList(ChatColor.GRAY + "Ban " + target.getName() + " for " + reason.get(player.getUniqueId())));
        kick.setItemMeta(kickMeta);

        invKick.setItem(4, kick);
        invKick.setItem(0, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(1, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(2, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(3, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(5, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(6, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(7, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(8, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        player.openInventory(invKick);
    }
    private void WarnCon(Player player, OfflinePlayer target){
        Inventory invKick = Bukkit.createInventory(null, 9, ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Confirm Warn");

        ItemStack kick = new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta kickMeta = kick.getItemMeta();
        kickMeta.setDisplayName(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Confirm Warn");
        kickMeta.setLore(Arrays.asList(ChatColor.GRAY + "Warn " + target.getName() + " for " + reason.get(player.getUniqueId())));
        kick.setItemMeta(kickMeta);

        invKick.setItem(4, kick);
        invKick.setItem(0, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(1, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(2, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(3, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(5, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(6, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(7, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        invKick.setItem(8, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        player.openInventory(invKick);
    }
    private void playerList(Player player, int page){

        Inventory gui = Bukkit.createInventory(null, 54, "Players - "+ page);

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
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Click to get info about the player"));
                meta.setLocalizedName(name);
                Skull.setItemMeta(meta);
                allItems.add(Skull);

            }

            ItemStack allPlayers = new ItemStack(Material.ENDER_PEARL);
            ItemMeta meta = allPlayers.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "All Players");
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Click to see all players"));
            meta.setLocalizedName("allplayers");
            allPlayers.setItemMeta(meta);

            gui.setItem(46, allPlayers);

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
    private void allPlayerList(Player player, int page){

        Inventory gui = Bukkit.createInventory(null, 54, "Players - "+ page);

        List<ItemStack> allItems = new ArrayList<>();
        for (int i = 0; i< allItems.size(); i++){
            allItems.remove(i);
        }

        ResultSet players = main.getPlayerManager().getAllPlayersResultSet();

        try {
            while (players.next()){
                String uuid = players.getString("uuid");
                String name = main.getPlayerManager().getPlayerUserName(uuid);

                ItemStack Skull = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) Skull.getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + name);
                meta.setOwner(name);
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Click to get info about the player"));
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
    public void onEnityClick(PlayerInteractEntityEvent e){
        if (inMenu.contains(e.getPlayer().getUniqueId())){
            if (inMenuList.contains(e.getPlayer().getUniqueId())){
               if (e.getRightClicked().getType().equals(EntityType.PLAYER)){
                   if (e.getHand().equals(EquipmentSlot.HAND)) {
                       switch (e.getPlayer().getInventory().getItemInHand().getItemMeta().getLocalizedName()) {
                           case "playerinfo":
                               playerInfo(e.getPlayer(), (Player) e.getRightClicked());
                               break;
                           case "freezeplayer":
                               if (!Bukkit.getPlayer(e.getRightClicked().getUniqueId()).hasPermission("therift.staff")) {
                                   if (freeze.contains(e.getRightClicked().getUniqueId())) {
                                       freeze.remove(e.getRightClicked().getUniqueId());
                                       e.getRightClicked().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                               ChatColor.RESET + ChatColor.GOLD + "You have been unfrozen by " + e.getPlayer().getDisplayName() +
                                               ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                                       e.getPlayer().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                               ChatColor.RESET + ChatColor.GOLD + "You have unfrozen " + e.getRightClicked().getName() +
                                               ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                                   } else if (!freeze.contains(e.getRightClicked())) {
                                       freeze.add(e.getRightClicked().getUniqueId());
                                       e.getRightClicked().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                               ChatColor.RESET + ChatColor.GOLD + "You have been frozen by " + e.getPlayer().getDisplayName() +
                                               ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                                       e.getPlayer().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                               ChatColor.RESET + ChatColor.GOLD + "You have frozen " + e.getRightClicked().getName() +
                                               ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                                   }
                               }else {
                                   e.getPlayer().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "―――――――――――――――――――――\n" +
                                           ChatColor.RESET + ChatColor.GOLD + "Can't freeze a staff mender" +
                                           ChatColor.GRAY + ChatColor.BOLD + "\n―――――――――――――――――――――");
                               }
                               break;
                       }
                   }
               }
            }
        }
    }
    public void onMove(PlayerMoveEvent e){
        if (freeze.contains(e.getPlayer().getUniqueId())){
            e.getPlayer().teleport(e.getPlayer().getLocation());
            e.getPlayer().sendMessage(ChatColor.RED + "You are frozen");
        }
    }
    public void onIntF(PlayerInteractEvent e){
        if (freeze.contains(e.getPlayer().getUniqueId())){
            e.setCancelled(true);
        }
    }
    private void playerInfo(Player player, OfflinePlayer target){

        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.BLUE.toString() + ChatColor.BOLD + "Player Info");

        //Discord Info
        ItemStack discordInfo = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta discordInfoMeta = (SkullMeta) discordInfo.getItemMeta();

            discordInfoMeta.setOwner("discord");
            discordInfoMeta.setDisplayName(ChatColor.BLUE + "Discord Info");
            ArrayList lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + " ");
            lore.add(ChatColor.GRAY + "Linked : " + main.getPlayerManager().isDiscordLinked(target.getUniqueId()));
            Long discordid = main.getPlayerManager().getDiscordID(target.getUniqueId());
            String discordname = main.getPlayerManager().getDiscordName(target.getUniqueId());
            if (discordid != null) {
                lore.add(ChatColor.GRAY + "DiscordID : " + discordid);
            }
            if (discordname != null) {
                lore.add(ChatColor.GRAY + "Discord name : " + discordname);
            }

            discordInfoMeta.setLore(lore);
            discordInfo.setItemMeta(discordInfoMeta);


        //Player Info
        ItemStack playerInfo = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playerInfoMeta = (SkullMeta) playerInfo.getItemMeta();
        playerInfoMeta.setOwner(target.getName());
        playerInfoMeta.setDisplayName(ChatColor.BLUE + "Player Info");
        ArrayList lore1 = new ArrayList<>();
        lore1.add(ChatColor.GRAY + " ");
        lore1.add(ChatColor.GRAY + "Is Banned : " + main.getPlayerManager().isActiveBanned(target.getUniqueId().toString()));
        lore1.add(ChatColor.GRAY + "UUID : " + target.getUniqueId());
        lore1.add(ChatColor.GRAY + "JoinData : " + main.getPlayerManager().getPlayerJoinDate(target.getUniqueId().toString()));
        lore1.add(ChatColor.GRAY + "Server : " + main.getPlayerManager().getServer(target.getName()));
        playerInfoMeta.setLore(lore1);
        playerInfo.setItemMeta(playerInfoMeta);

        //Punish player
        ItemStack playerPunish = new ItemStack(Material.ANVIL);
        ItemMeta playerPunishMeta = playerPunish.getItemMeta();
        playerPunishMeta.setDisplayName(ChatColor.RED + "Punish Player");
        playerPunishMeta.setLore(Arrays.asList(ChatColor.GRAY + "Punish a player"));
        playerPunishMeta.setLocalizedName(target.getName());
        playerPunish.setItemMeta(playerPunishMeta);

        //History
        ItemStack punishHistory = new ItemStack(Material.PAPER);
        ItemMeta punishHistoryMeta = playerPunish.getItemMeta();
        punishHistoryMeta.setDisplayName(ChatColor.BLUE + "Punish History");
        ArrayList lore2 = new ArrayList<>();

        try {
            ResultSet rs = main.getPlayerManager().getPunishHistory(target.getUniqueId().toString());
            while (rs.next()){
                lore2.add(ChatColor.GRAY + " ");
                lore2.add(ChatColor.GRAY + "Punish Type : " + rs.getString("PunishType"));
                lore2.add(ChatColor.GRAY + "Punish By : " + rs.getString("PunishedBy"));
                lore2.add(ChatColor.GRAY + "Reason : " + rs.getString("reason"));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        punishHistoryMeta.setLore(lore2);
        punishHistory.setItemMeta(punishHistoryMeta);

        //To server
        ItemStack toServer = new ItemStack(Material.COMPASS);
        ItemMeta toServerMeta = toServer.getItemMeta();
        toServerMeta.setDisplayName(ChatColor.BLUE + "Go to server");
        toServerMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to go to there server"));
        toServerMeta.setLocalizedName(target.getName());
        toServer.setItemMeta(toServerMeta);

        //To player
        ItemStack toPlayer = new ItemStack(Material.ENDER_PEARL);
        ItemMeta toPlayerMeta = toPlayer.getItemMeta();
        toPlayerMeta.setDisplayName(ChatColor.BLUE + "Go to Player");
        toPlayerMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to go to the player"));
        toPlayerMeta.setLocalizedName(target.getName());
        toPlayer.setItemMeta(toPlayerMeta);


        //Setting Items
        inv.setItem(8, punishHistory);
        inv.setItem(5, playerPunish);
        inv.setItem(4, playerInfo);
        inv.setItem(3, discordInfo);
        inv.setItem(0, toServer);
        inv.setItem(1, toPlayer);
        player.openInventory(inv);

    }
    private void playerPunish(Player player, OfflinePlayer target){
        Inventory punishInv = Bukkit.createInventory(null, 9, ChatColor.RED.toString() + ChatColor.BOLD + "Punish Player");

        ItemStack kick = new ItemStack(Material.FEATHER);
        ItemMeta kickMeta = kick.getItemMeta();
        kickMeta.setDisplayName(ChatColor.AQUA + "Kick");
        kickMeta.setLore(Arrays.asList(ChatColor.GRAY + "Kick the player"));
        kickMeta.setLocalizedName(target.getName());
        kick.setItemMeta(kickMeta);

        ItemStack ban = new ItemStack(Material.ANVIL);
        ItemMeta banMeta = ban.getItemMeta();
        banMeta.setDisplayName(ChatColor.AQUA + "Ban");
        banMeta.setLore(Arrays.asList(ChatColor.GRAY + "Ban the player"));
        banMeta.setLocalizedName(target.getName());
        ban.setItemMeta(banMeta);

        ItemStack tempban = new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta tempbanMeta = tempban.getItemMeta();
        tempbanMeta.setDisplayName(ChatColor.AQUA + "Warn");
        tempbanMeta.setLore(Arrays.asList(ChatColor.GRAY + "Warn the player"));
        tempbanMeta.setLocalizedName(target.getName());
        tempban.setItemMeta(tempbanMeta);

        ItemStack mute = new ItemStack(Material.BARRIER);
        ItemMeta muteMeta = mute.getItemMeta();
        muteMeta.setDisplayName(ChatColor.AQUA + "Mute");
        muteMeta.setLore(Arrays.asList(ChatColor.GRAY + "Mute the player"));
        muteMeta.setLocalizedName(target.getName());
        mute.setItemMeta(muteMeta);


        punishInv.setItem(3, kick);
        punishInv.setItem(4, ban);
        punishInv.setItem(5, tempban);
        punishInv.setItem(6, mute);

        if (player.getOpenInventory() == null){
            player.closeInventory();
        }
        player.openInventory(punishInv);
    }
    public void setVanish(Player player){

        if (!vanish){

            main.getPlayerManager().setInVanish(player.getUniqueId(), true);

            if (TagTab.tabs.containsKey(player.getUniqueId())){
                TagTab.tabs.get(player.getUniqueId()).remove();
                TagTab.tabs.remove(player.getUniqueId());
            }

            //to vanish
            for (Player player1 : Bukkit.getOnlinePlayers()){
                if (!player1.hasPermission("therift.staff")) {
                    player1.hidePlayer(player);

                } else {
                    if (player1 != player) {
                        player.sendMessage(ChatColor.RED + "Didn't vanish for " + player1.getDisplayName());
                    }
                }
            }

            ItemStack isVanished = new ItemStack(Material.GRAY_DYE);
            ItemMeta isVanishedMeta = isVanished.getItemMeta();
            isVanishedMeta.setDisplayName(ChatColor.GREEN + "Vanished");
            isVanishedMeta.setLocalizedName("vanish");
            isVanishedMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to vanish"));
            isVanished.setItemMeta(isVanishedMeta);

            player.getInventory().setItem(0, isVanished);

            player.sendMessage(ChatColor.GREEN + "You are now vanished");
            Vanished.add(player.getUniqueId());
            vanish = true;
        } else if (vanish) {

            main.getPlayerManager().setInVanish(player.getUniqueId(), false);

            ArmorStand slime = (ArmorStand) player.getWorld().spawnEntity(player.getEyeLocation().subtract(0, 2, 0), EntityType.ARMOR_STAND);
            slime.setSmall(true);
            slime.setInvisible(true);
            slime.setInvulnerable(true);
            slime.setBasePlate(false);
            slime.setGravity(false);
            slime.setHeadPose(new EulerAngle(0, 2, 0));

            User user = main.getApi().getUserManager().getUser(player.getUniqueId());
            String a = user.getCachedData().getMetaData().getPrimaryGroup();
            Group group = main.getApi().getGroupManager().getGroup(a);
            String prefix = group.getDisplayName();
            prefix.replace(" ", "");


            slime.setCustomName(ChatColor.translateAlternateColorCodes('&', prefix));
            slime.setCustomNameVisible(true);
            slime.setPersistent(true);
            player.setPassenger(slime);
            TagTab.tabs.put(player.getUniqueId(), slime);

            player.hideEntity(main, slime);

            //Unvanish
            for (Player player1 : Bukkit.getOnlinePlayers()){
                if (!player1.hasPermission("therift.staff")) {
                    player1.showPlayer(player);
                }
            }
            ItemMeta isVanishedMeta = isVanished.getItemMeta();
            isVanishedMeta.setDisplayName(ChatColor.GREEN + "Vanish");
            isVanishedMeta.setLocalizedName("vanish");
            isVanishedMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to vanish"));
            isVanished.setItemMeta(isVanishedMeta);

            player.getInventory().setItem(0, isVanished);

            player.sendMessage(ChatColor.RED + "You are now unvanished");
            Vanished.remove(player.getUniqueId());
            vanish = false;
        }
    }
    public void updateVanish(PlayerJoinEvent e){
        if (!e.getPlayer().hasPermission("therift.staff")){
            if(!Vanished.isEmpty()){
                for (int i = 0; i < Vanished.size(); i++){
                    Player player = Bukkit.getPlayer(Vanished.get(i));
                    e.getPlayer().hidePlayer(player);
                }
            }
        }
    }
    public void onDrop(PlayerDropItemEvent e){
        if (inMenu.contains(e.getPlayer().getUniqueId())) {
            if (inMenuList.contains(e.getPlayer().getUniqueId())) {
                e.setCancelled(true);
            }
        }
        if (freeze.contains(e.getPlayer().getUniqueId())){
            e.setCancelled(true);
        }
    }
    public void onPickup(PlayerPickupItemEvent e){
        if (inMenu.contains(e.getPlayer().getUniqueId())) {
            if (inMenuList.contains(e.getPlayer().getUniqueId())) {
                e.setCancelled(true);
            }
        }
        if (freeze.contains(e.getPlayer().getUniqueId())){
            e.setCancelled(true);
        }
    }
}
