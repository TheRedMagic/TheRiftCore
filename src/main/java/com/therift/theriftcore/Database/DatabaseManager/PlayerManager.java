package com.therift.theriftcore.Database.DatabaseManager;

import com.therift.theriftcore.TheRiftCore;
import com.therift.theriftcore.Discord.DiscordListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class PlayerManager {

    private TheRiftCore main;
    public PlayerManager(TheRiftCore main){
        this.main = main;
    }

    private List<String> OnlinePlayerL = new ArrayList<>();
    private int rowCount;
    private ResultSet OnlinePlayers;
    private ResultSet DatePlayer;

    public ResultSet getOnlineResultset() {
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT UUID FROM PlayerData WHERE ONLINE = 1;");
            OnlinePlayers = ps.executeQuery();
        } catch (SQLException e) {

        }

        return OnlinePlayers;
    }

    public boolean isMute(UUID uuid){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT ISMUTE FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                if (rs.getBoolean("ISMUTE")){
                    return true;
                }else {
                    return false;
                }
            }

        } catch (SQLException e){
            throw  new  RuntimeException(e);
        }
        return false;
    }
    public void setMute(UUID uuid, Boolean mute){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET ISMUTE = ? WHERE UUID = ?");
            ps.setBoolean(1, mute);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void addDiscordPunish(String id, String name, String type, String info){
        try{
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("INSERT INTO DiscordPunishLog (DiscordID, DiscordName, PunishType, Info) VALUES (?,?,?,?)");
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, type);
            ps.setString(4, info);
            ps.executeUpdate();
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }
    public List<String> getOnlineList() {
        List<String> uuid = new ArrayList<>();
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT USERNAME FROM PlayerData WHERE ONLINE = 1;");
            OnlinePlayers = ps.executeQuery();
            while (OnlinePlayers.next()){
                uuid.add(OnlinePlayers.getString("username"));
            }
        } catch (SQLException e) {

        }

        return uuid;
    }

    public Boolean isPlayerOnline(UUID uuid){
        try{
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT ONLINE FROM PlayerData WHERE UUID = ?");
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

    public Date getPlayerJoinDate(String uuid){
        if (uuid != null) {
            try {
                PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT FirstJoinDate FROM PlayerData WHERE UUID = ?");
                ps.setString(1, uuid);
                DatePlayer = ps.executeQuery();
                while (DatePlayer.next()){
                    return DatePlayer.getDate("FirstJoinDate");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return null;
    }

    public String getPlayerUserName(String uuid){
        try{
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT USERNAME FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid);
            ResultSet username = ps.executeQuery();
            while (username.next()){
                return username.getString("username");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<String> getAllPlayersList(){
        try{
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT USERNAME FROM PlayerData");
            ResultSet rs = ps.executeQuery();
            List<String> players = new ArrayList<>();
            while (rs.next()){
                players.add(rs.getString("Username"));
            }
            return players;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void setBan(UUID uuid, Boolean booleanB){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET BANNED = ? WHERE UUID = ?");
            ps.setBoolean(1, booleanB);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public List<String> getAllBannedPlayers(){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT USERNAME FROM PlayerData WHERE BANNED = 1");
            ResultSet rs = ps.executeQuery();
            List<String> players = new ArrayList<>();
            while (rs.next()){
                players.add(rs.getString("username"));
            }
            return players;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void addToPunishLog(String uuid, String Username, String PunishType, String Reason, String PunishedBy, Boolean Active){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("INSERT INTO PunishData (UUID, Username, PunishType, Reason, PunishedBy, Active) VALUES (?,?,?,?,?,?)");
            ps.setString(1, uuid);
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

    public ResultSet getPunishHistory(String uuid){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT * FROM PunishData WHERE UUID = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            return rs;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public ResultSet getDiscordPunishHistory(String id){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT * FROM DiscordPunishLog WHERE DiscordID = ?");
            ps.setString(1, id);
            ResultSet resultSet = ps.executeQuery();
            return resultSet;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void setActive(String uuid, Boolean Active){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PunishData SET ACTIVE = ? WHERE UUID = ?");
            ps.setBoolean(1, Active);
            ps.setString(2, uuid);
            ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public ResultSet getAllPlayersResultSet(){
        try{
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT UUID FROM PlayerData");
            ResultSet rs = ps.executeQuery();
            return rs;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public boolean isDiscordLinked(UUID uuid){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT DiscordID FROM PlayerData WHERE UUID = ?");
            ps.setString(1, String.valueOf(uuid));
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String s = rs.getString("DiscordID");
                if (s.equals("0")){
                    return false;
                }else{
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public void addWarnings(UUID uuid){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT Warnings FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            int warnings = 0;
            while (rs.next()){
                warnings = rs.getInt("Warnings");
            }

            PreparedStatement ps1 = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET Warnings = ? WHERE UUID = ?");
            ps1.setInt(1, warnings + 1);
            ps1.setString(2, uuid.toString());
            ps1.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public long getDiscordID(UUID uuid){
        PreparedStatement ps = null;
        try {
            ps = main.getDatabase().getConnection().prepareStatement("SELECT DiscordID FROM PlayerData WHERE UUID = ?");
            ps.setString(1, String.valueOf(uuid));
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return Long.parseLong(rs.getString("DiscordID"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    public void setInStaff(UUID uuid, Boolean InStaff){
        try{
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET InStaff = ? WHERE UUID = ?");
            ps.setBoolean(1, InStaff);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public List<UUID> getOnlineStaff(){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT UUID FROM PlayerData WHERE ONLINE = 1 AND RANK = ? OR RANK = ? OR RANK = ? OR RANK = ? OR RANK = ? OR RANK = ?");
            ps.setString(1, "media");
            ps.setString(2, "staff");
            ps.setString(3, "admin");
            ps.setString(4, "dev");
            ps.setString(5, "owner");
            ps.setString(6, "mod");
            ResultSet rs = ps.executeQuery();
            List<UUID> players = new ArrayList<>();

            while (rs.next()){
                players.add(UUID.fromString(rs.getString("UUID")));
            }
            return players;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public int getTotalPlayers(){
        try{
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT UUID FROM PlayerData WHERE ONLINE = 1");
            ResultSet rs = ps.executeQuery();
            int totPlayers = 0;
            while (rs.next()){
                totPlayers++;
            }
            return totPlayers;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public int getTotalStaff(){
        try{
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT UUID FROM PlayerData WHERE ONLINE = 1 AND RANK = ? OR RANK = ? OR RANK = ? OR RANK = ?");
            ps.setString(1, "staff");
            ps.setString(2, "mod");
            ps.setString(3, "admin");
            ps.setString(4, "owner");
            ResultSet rs = ps.executeQuery();
            int totPlayers = 0;
            while (rs.next()){
                totPlayers++;
            }
            return totPlayers;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void setRank(UUID uuid, String rank){
        try{
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET RANK = ? WHERE UUID = ?");
            ps.setString(1, rank);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void setInVanish(UUID uuid, Boolean InVanish){
        try{
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET InVanish = ? WHERE UUID = ?");
            ps.setBoolean(1, InVanish);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public Boolean inVanish(UUID uuid){
        try{
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
    public Boolean inStaff(UUID uuid){
        try{
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
    public String getDiscordName(UUID uuid){
        PreparedStatement ps = null;
        try {
            ps = main.getDatabase().getConnection().prepareStatement("SELECT DiscordName FROM PlayerData WHERE UUID = ?");
            ps.setString(1, String.valueOf(uuid));
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getString("DiscordName");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void setServer(UUID uuid, String name){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET SERVER = ? WHERE UUID = ?");
            ps.setString(1, name);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public String getServer(String name){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT SERVER FROM PlayerData WHERE USERNAME = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getString("SERVER");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }


}
