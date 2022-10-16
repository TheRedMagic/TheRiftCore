package com.therift.theriftcore.Core;

import com.therift.theriftcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.BatchUpdateException;

public class Tab {
    private Main main;
    public Tab(Main main){
        this.main = main;
    }


    public void setTab(){
        for (Player player1 : Bukkit.getOnlinePlayers()){
            int totPlayers = main.getPlayerManager().getTotalPlayers();
            int totStaff = main.getPlayerManager().getTotalStaff();

            player1.setPlayerListHeader("Total Online Players : " + totPlayers + "\nTotal Online Staff : " + totStaff);
        }
    }
}
