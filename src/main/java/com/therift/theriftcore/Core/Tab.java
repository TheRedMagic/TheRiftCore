package com.therift.theriftcore.Core;

import com.therift.theriftcore.TheRiftCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Tab {
    private TheRiftCore main;
    public Tab(TheRiftCore main){
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
