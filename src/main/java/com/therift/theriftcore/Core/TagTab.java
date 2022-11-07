package com.therift.theriftcore.Core;

import com.therift.theriftcore.TheRiftCore;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.EulerAngle;

import java.util.HashMap;
import java.util.UUID;

public class TagTab {
    private static TheRiftCore main;
    public static HashMap<UUID, ArmorStand> tabs = new HashMap<UUID, ArmorStand>();
    public TagTab(TheRiftCore main){
        this.main = main;
    }

    public static void setNameTag(Player player){
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        setTag(player, main.getApi().getGroupManager().getGroup("default"), "h");
        setTag(player, main.getApi().getGroupManager().getGroup("staff"), "g");
        setTag(player, main.getApi().getGroupManager().getGroup("mod"), "f");
        setTag(player, main.getApi().getGroupManager().getGroup("owner"), "a");
        setTag(player, main.getApi().getGroupManager().getGroup("media"), "d");
        setTag(player, main.getApi().getGroupManager().getGroup("dev"), "c");
        setTag(player, main.getApi().getGroupManager().getGroup("builder"), "e");
        setTag(player, main.getApi().getGroupManager().getGroup("admin"), "b");
    }
    public static void newTag(){
        for (Player target : Bukkit.getOnlinePlayers()) {
            User user = main.getApi().getUserManager().getUser(target.getUniqueId());
            switch (user.getPrimaryGroup()) {
                case "default":
                    newtagupdate(target, "default", "h");
                    break;
                case "staff":
                    newtagupdate(target, "staff", "g");
                    break;
                case "mod":
                    newtagupdate(target, "mod", "f");
                    break;
                case "owner":
                    newtagupdate(target, "owner", "a");
                    break;
                case "media":
                    newtagupdate(target, "media", "d");
                    break;
                case "dev":
                    newtagupdate(target, "dev", "c");
                    break;
                case "builder":
                    newtagupdate(target, "builder", "e");
                    break;
                case "admin":
                    newtagupdate(target, "admin", "b");
                    break;
            }
        }

    }
    public static void removeTag(Player player){
        for (Player target : Bukkit.getOnlinePlayers()){
            target.getScoreboard().getEntryTeam(player.getName()).removeEntry(player.getName());
        }
    }

    public static void onJoin(Player player) {
        String rank = main.getApi().getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();


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
        if (!rank.equals("default")) {
            slime.setCustomNameVisible(true);
        }
        slime.setPersistent(true);
        player.setPassenger(slime);
        tabs.put(player.getUniqueId(), slime);

        player.hideEntity(main, slime);
    }

    public static void onQuit(Player player){
        if (tabs.containsKey(player.getUniqueId())){
            tabs.get(player.getUniqueId()).remove();
            tabs.remove(player.getUniqueId());
        }
    }

    private static void newtagupdate(Player player, String Name, String Order){
        Group group = main.getApi().getGroupManager().getGroup(Name);
        for (Player target : Bukkit.getOnlinePlayers()){
            target.getScoreboard().getTeam(Order + group.getName()).addEntry(player.getName());
        }
    }

    private static void setTag(Player player, Group rank, String Order){
        Team team = player.getScoreboard().registerNewTeam(Order + rank.getName());
        String name = rank.getDisplayName();

        User user = main.getApi().getUserManager().getUser(player.getUniqueId());
        String a = user.getCachedData().getMetaData().getPrimaryGroup();
        Group group = main.getApi().getGroupManager().getGroup(a);
        String prefix = group.getDisplayName();


        switch (rank.getName()) {
            case "default":
                team.setColor(ChatColor.GRAY);

                break;
            case "staff":
                team.setColor(ChatColor.AQUA);
                player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', prefix + player.getDisplayName()));
                break;
            case "mod":
                team.setColor(ChatColor.DARK_BLUE);
                player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', prefix + player.getDisplayName()));
                break;
            case "owner":
                team.setColor(ChatColor.LIGHT_PURPLE);
                player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', prefix + player.getDisplayName()));
                break;
            case "media":
                team.setColor(ChatColor.YELLOW);
                player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', prefix + player.getDisplayName()));
                break;
            case "dev":
                team.setColor(ChatColor.YELLOW);
                player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', prefix + player.getDisplayName()));
                break;
            case "builder":
                team.setColor(ChatColor.GREEN);
                player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', prefix + player.getDisplayName()));
                break;
            case "admin":
                team.setColor(ChatColor.RED);
                player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', prefix + player.getDisplayName()));
                break;
        }
    }

}
