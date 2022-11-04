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
        },72000, 72000);
    }
    private HashMap<String, Integer> Amount = new HashMap<>();

    public void onChat(MessageReceivedEvent e, Guild guild){
        this.guild = guild;
        if(!e.getMember().getUser().isBot()) {
            if (Amount.containsKey(e.getMember().getId())) {
                Integer amount = Amount.get(e.getMember().getId());
                Amount.put(e.getMember().getId(), amount+1);
            } else {
                Integer amount = 0;
                try {
                    PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT MessagesSend FROM DiscordUserInfo WHERE DiscordID = ?");
                    ps.setString(1, e.getMember().getId());
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        amount = Integer.valueOf(rs.getInt("MessagesSend"));
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                Amount.put(e.getMember().getId(), amount + 1);
            }
        }
    }

    public void saveAmount(){
        if (!Amount.isEmpty()){
            for(String ID : Amount.keySet()) {
                if (guild.getMemberById(ID) != null) {
                    Integer amount = this.Amount.get(ID);

                    try {
                        PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT * FROM DiscordUserInfo WHERE DiscordID = ?");
                        ps.setString(1, ID);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            PreparedStatement ps1 = main.getDatabase().getConnection().prepareStatement("UPDATE DiscordUserInfo SET MessagesSend = ? WHERE DiscordID = ?");
                            ps1.setInt(1, amount);
                            ps1.setString(2, ID);
                            ps1.executeUpdate();
                        } else {
                            PreparedStatement ps2 = main.getDatabase().getConnection().prepareStatement("INSERT INTO DiscordUserInfo (DiscordID, MessagesSend) VALUES (?,?)");
                            ps2.setString(1, ID);
                            ps2.setInt(2, amount);
                            ps2.executeUpdate();
                        }

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    Amount.remove(ID);

                }
            }
        }
    }

    public HashMap<String, Integer> getAmount() {
        return Amount;
    }
}
