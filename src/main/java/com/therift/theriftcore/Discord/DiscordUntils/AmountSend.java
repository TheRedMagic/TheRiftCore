package com.therift.theriftcore.Discord.DiscordUntils;

import com.therift.theriftcore.Discord.DiscordListener;
import com.therift.theriftcore.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.Bukkit;
import org.checkerframework.checker.units.qual.A;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AmountSend {
    private Main main;
    private Guild guild;
    public AmountSend(Main main){
        this.main = main;
        Bukkit.getScheduler().runTaskTimer(main, () -> {
            saveAmount();
        },20, 20);
    }
    private HashMap<String, Integer> Amount = new HashMap<>();

    public void onChat(MessageReceivedEvent e, Guild guild){
        this.guild = guild;
        if(!e.getMember().getUser().isBot()) {
            int amount = 0;
            try {
                System.out.println(e.getMember().getId() +"|"+ amount);
                PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT MessagesSend FROM DiscordUserInfo WHERE DiscordID = ?");
                ps.setString(1, e.getMember().getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                     amount = rs.getInt("MessagesSend");
                }
                System.out.println(e.getMember().getId() +"|"+ amount);

                PreparedStatement ps3 = main.getDatabase().getConnection().prepareStatement("SELECT * FROM DiscordUserInfo WHERE DiscordID = ?");
                ps3.setString(1, e.getMember().getUser().getId());
                ResultSet rs3 = ps3.executeQuery();
                if (rs3.next()) {
                    PreparedStatement ps1 = main.getDatabase().getConnection().prepareStatement("UPDATE DiscordUserInfo SET MessagesSend = ? WHERE DiscordID = ?");
                    ps1.setInt(1, amount+1);
                    ps1.setString(2, e.getMember().getUser().getId());
                    ps1.executeUpdate();
                } else {
                    PreparedStatement ps2 = main.getDatabase().getConnection().prepareStatement("INSERT INTO DiscordUserInfo (DiscordID, MessagesSend) VALUES (?,?)");
                    ps2.setString(1, e.getMember().getUser().getId());
                    ps2.setInt(2, amount+1);
                    ps2.executeUpdate();
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }


        }
    }

    public void saveAmount(){
        main.getDiscordCounter().onSave();
    }

    public HashMap<String, Integer> getAmount() {
        return Amount;
    }
}
