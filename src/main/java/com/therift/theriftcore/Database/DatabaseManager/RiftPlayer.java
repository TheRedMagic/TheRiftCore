package com.therift.theriftcore.Database.DatabaseManager;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.therift.theriftcore.Discord.DiscordListener;
import com.therift.theriftcore.TheRiftCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.awt.*;
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class RiftPlayer {
    private TheRiftCore main;
    private UUID uuid;
    private String name;
    private OfflinePlayer player;

    public RiftPlayer(UUID uuid){
        main = TheRiftCore.main;
        if (main == null){
            Bukkit.getLogger().info("RiftPlayer | Main Class Null");
            return;
        }
        if (Bukkit.getOfflinePlayer(uuid).isOnline() && Bukkit.getOfflinePlayer(uuid) != null) {
            this.player = Bukkit.getOfflinePlayer(uuid);
            return;
        }
        this.uuid = uuid;
        this.name = player.getName();

    }

    public void playSound(Sound sound, float volume, float pitch){
        if (player.isOnline()){
            player.getPlayer().playSound(player.getPlayer(), sound, SoundCategory.AMBIENT, volume, pitch);
        }
    }
    public UUID getUuid(){return uuid;}
    public void kickPlayer(String Reason){
        String text = ChatColor.translateAlternateColorCodes('&', Reason);
        if (player.isOnline()){
            player.getPlayer().kickPlayer(text);
        }
    }
    public String getName(){return name;}
    public OfflinePlayer getOfflinePlayer() {return player;}
    public Player getOnlinePlayer(){
        if (player.isOnline()) {
            return player.getPlayer();
        }
        return null;
    }
    public Location getLocation(){
        if (player.isOnline()) {
            return player.getPlayer().getLocation();
        }
        return null;
    }
    public World getWorld(){if (player.isOnline())
    {
        return player.getPlayer().getWorld();
    }
    return null;
    }
    public Material getStandingBlock(){
        if (player.isOnline()){
            Player player1 = player.getPlayer();
            return player1.getLocation().subtract(0, 1, 0).getBlock().getType();
        }
        return null;
    }
    public void addPotionEffect(PotionEffect effect){
        if (player.isOnline()) {
            player.getPlayer().addPotionEffect(effect);
        }
    }
    public String getRank(){
        return main.getApi().getUserManager().getUser(uuid).getPrimaryGroup();
    }
    public void setRank(String rank){
        main.getApi().getUserManager().getUser(uuid).setPrimaryGroup(rank);
    }
    public Boolean isDiscordLinked(){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT DiscordID FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String id = rs.getString("DiscordID");
                if (id != String.valueOf(0)){
                    return false;
                } else {
                    return true;
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
    public int getPing(){
        if (player.isOnline()) {
            return player.getPlayer().getPing();
        }
        return 0;
    }
    public boolean hasPermission(String permission){
        if (player.isOnline()) {
            return false;
        }
        if (player.getPlayer().hasPermission(permission)){ return true; }else {
            return false;
        }
    }
    public void preformCommand(String command){
        if (player.isOnline()) {
            player.getPlayer().performCommand(command);
        }
    }
    public void setTexturePack(String url){
        if (player.isOnline()) {
            player.getPlayer().setTexturePack(url);
        }
    }
    public boolean inSneaking(){
        if (player.isOnline()) {
            if (player.getPlayer().isSneaking()){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }
    public String getDiscordID(){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT DiscordID FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getString("DiscordId");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
    public void setDiscordID(Integer ID){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET DiscordID = ? WHERE UUID = ?");
            ps.setString(1, ID.toString());
            ps.setString(2, uuid.toString());
            ps.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public String getDiscordName(){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT DiscordName FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getString("DiscordName");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
    public void setDiscordName(String name){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET DiscordName = ? WHERE UUID = ?");
            ps.setString(1, name);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public Boolean isOnline(){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT Online FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getBoolean("Online");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
    public void setOnline(Boolean online){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET Online = ? WHERE UUID = ?");
            ps.setBoolean(1, online);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public String getServer(){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT Server FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getString("Server");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
    public void setServer(String server){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET Server = ? WHERE UUID = ?");
            ps.setString(1, server);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public Boolean isBanned(){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT Banned FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getBoolean("Banned");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
    public void setBanned(Boolean banned){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET Banned = ? WHERE UUID = ?");
            ps.setBoolean(1, banned);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();

            PreparedStatement ps1 = main.getDatabase().getConnection().prepareStatement("UPDATE PunishData SET ACTIVE = ? WHERE UUID = ?");
            ps1.setBoolean(1, true);
            ps1.setString(2, uuid.toString());
            ps1.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public int getWarningAmount(){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT Warnings FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getInt("Warnings");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return 0;
    }
    public void addWarningAmount(){
        int amount = getWarningAmount();
        amount = amount + 1;

        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET Server = ? WHERE UUID = ?");
            ps.setInt(1, amount);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void removeWarningsAmount(){
        int amount = getWarningAmount();
        if (amount != 0) {
            amount = amount - 1;
        } else {
            return;
        }

        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET Server = ? WHERE UUID = ?");
            ps.setInt(1, amount);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public Boolean isMute(){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT isMute FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getBoolean("isMute");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
    public void setMute(Boolean mute){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET isMute = ? WHERE UUID = ?");
            ps.setBoolean(1, mute);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public Boolean inStaffMode(){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT InStaff FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getBoolean("InStaff");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
    public void setStaffMode(Boolean staffMode){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET InStaff = ? WHERE UUID = ?");
            ps.setBoolean(1, staffMode);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public Boolean inVanish(){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT InVanish FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getBoolean("InVanish");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
    public void setInVanish(Boolean vanish){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET InVanish = ? WHERE UUID = ?");
            ps.setBoolean(1, vanish);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void movePlayerToServer(String serverName){
        if (player.isOnline()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(serverName);
            player.getPlayer().sendPluginMessage(main, "BungeeCord", out.toByteArray());
        }
    }
    public void teleport(Location location){
        if (player.isOnline()) {
            player.getPlayer().teleport(location);
        }

    }
    public void saveData(){
        if (player.isOnline()) {
            player.getPlayer().saveData();
        }
    }
    public void loadData(){}
    public void sendMessage(String message){
        String text = ChatColor.translateAlternateColorCodes('&', message);
        if (player.isOnline()) {
            player.getPlayer().sendMessage(text);
        }
    }
    public Scoreboard getScoreBoard(){
        if (player.isOnline()) {
            return player.getPlayer().getScoreboard();
        }
        return null;
    }
    public void setVelocity(Vector velocity){
        if (player.isOnline()) {
            player.getPlayer().setVelocity(velocity);
        }
    }
    @Deprecated
    public void hidePlayer(Player player){
        player.hidePlayer(main, player);
    }
    @Deprecated
    public ResultSet getPlayerPunishHistory() throws SQLException{
        PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT * FROM PunishData WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();
        return rs;
    }
    public void addPlayerPunishHistory(String uuid, String Username, String PunishType, String Reason, String PunishedBy, Boolean Active){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("INSERT INTO PunishData (UUID, Username, PunishType, Reason, PunishedBy, Active) VALUES (?,?,?,?,?,?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, Username);
            ps.setString(3, PunishType);
            ps.setString(4, Reason);
            ps.setString(5, PunishedBy);
            ps.setBoolean(6, Active);
            ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        TextChannel textChannel = DiscordListener.jda.getGuildById(main.getConfig().getString("GuildID")).getTextChannelById(main.getConfig().getString("PunishLogId"));
        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Punish Log").setDescription("Username : " + Username +
                "\nPunishType : " + PunishType + "\nPunishBy : " + PunishedBy + "\nReason : " + Reason);
        textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
    }
    public boolean isActiveBanned(String uuid){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT ACTIVE FROM PunishData WHERE UUID = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                if (rs.getBoolean("active")){
                    return true;
                }
                return false;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return false;
    }
    public void setActiveBan(String uuid, Boolean Active){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PunishData SET ACTIVE = ? WHERE UUID = ?");
            ps.setBoolean(1, Active);
            ps.setString(2, uuid);
            ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public java.sql.Date getFirstJoinDate(){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT FirstJoinDate FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getDate("FirstJoinDate");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
    public java.sql.Date getLastJoinDate(){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT LastJoinDate FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getDate("LastJoinDate");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
    public void setLastJoinDate(java.sql.Date date){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET LastJoinDate = ? WHERE UUID = ?");
            ps.setDate(1, date);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void sendPrivateDiscordMessage(MessageEmbed embed){
        User user = DiscordListener.jda.getUserById(getDiscordID());
        user.openPrivateChannel().complete()
                .sendMessageEmbeds(embed).queue();
    }
}
