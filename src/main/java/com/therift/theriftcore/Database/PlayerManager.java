package com.therift.theriftcore.Database;

import com.therift.theriftcore.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayerManager {

    private Main main;
    public PlayerManager(Main main){
        this.main = main;
    }

    private List<String> OnlinePlayerL = new ArrayList<>();
    private int rowCount;
    private ResultSet OnlinePlayers;
    private ResultSet DatePlayer;

    public ResultSet getOnlineList() {
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT UUID FROM PlayerData WHERE ONLINE = 1;");
            OnlinePlayers = ps.executeQuery();
        } catch (SQLException e) {

        }

        return OnlinePlayers;
    }

    public ResultSet getPlayerJoinDate(String uuid){
        if (uuid != null) {
            try {
                PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT FirstJoinDate FROM PlayerData WHERE UUID = ?");
                ps.setString(1, uuid);
                DatePlayer = ps.executeQuery();
                return DatePlayer;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return null;
    }
}
