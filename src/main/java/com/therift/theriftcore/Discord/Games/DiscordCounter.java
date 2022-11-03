package com.therift.theriftcore.Discord.Games;

import com.google.gson.Gson;
import com.therift.theriftcore.Discord.DiscordListener;
import com.therift.theriftcore.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.Bukkit;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

public class DiscordCounter {
    private Main main;
    private int count;
    private int CountHighsource;
    private String ID = null;
    private HashMap<String, Integer> corretCounded = new HashMap<>();
    private HashMap<String, Integer> wrongCounded = new HashMap<>();


    public DiscordCounter(Main main) {
        this.main = main;
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
            try {
                PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT * FROM DiscordInfo WHERE InfoType = ?");
                ps.setString(1, "Count");
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    this.count = Integer.valueOf(rs.getString("CountNumder"));
                }else {
                    PreparedStatement ps1 = main.getDatabase().getConnection().prepareStatement("INSERT INTO DiscordInfo (InfoType, CountNumder) VALUES (?,?)");
                    ps1.setString(1, "Count");
                    ps1.setString(2, "0");
                    ps1.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT * FROM DiscordInfo WHERE InfoType = ?");
                ps.setString(1, "HighScore");
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    this.CountHighsource = Integer.valueOf(rs.getString("CountNumder"));
                    if (rs.getString("LastCounted") != null) {
                        this.ID = rs.getString("LastCounted");
                    }
                }else {
                    PreparedStatement ps1 = main.getDatabase().getConnection().prepareStatement("INSERT INTO DiscordInfo (InfoType, CountNumder) VALUES (?,?)");
                    ps1.setString(1, "HighScore");
                    ps1.setString(2, "0");
                    ps1.executeUpdate();
                }

                HighScoreCommand(DiscordListener.jda.getGuildById("997076075509194795"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }, 20);

        Bukkit.getScheduler().runTaskTimer(main, () -> {
            onSave();
        }, 7220, 7200);

    }

    public void HighScoreCommand(Guild guild){
        guild.upsertCommand("count-high-score", "The biggest count in the server").queue();
    }

    public void onCommand(SlashCommandInteractionEvent e){
        if (e.getName().equals("count-high-score")){
            TextChannel textChannel = e.getGuild().getTextChannelById("1034220609909043230");
            if (e.getChannel().equals(textChannel)){


                e.deferReply().queue();
                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.BLUE).setAuthor("TheRift")
                        .setTitle("Count HighScore")
                        .setDescription("Last counted by : " + ID + "\nNumber counted to : " + CountHighsource);
                e.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
            }else {
                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.RED).setAuthor("TheRift")
                        .setTitle("Wrong Channel")
                        .setDescription("Please use this command in #\uD83D\uDD22counting");
                e.deferReply(true).queue();
                e.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
            }
        }
    }
    public void onChat(MessageReceivedEvent e){
        TextChannel textChannel = e.getGuild().getTextChannelById("1034220609909043230");
        if (e.getChannel().equals(textChannel)){

            String firstWord;
            if (e.getMessage().getContentRaw().contains(" ")) {
                firstWord = e.getMessage().getContentRaw().split(" ")[0];
            }else {
                firstWord = e.getMessage().getContentRaw();
            }

            try {
                int numder = Integer.parseInt(firstWord);

                String name = null;
                try {
                    PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT LastCounted FROM DiscordInfo WHERE InfoType = ?");
                    ps.setString(1, "Count");
                    ResultSet resultSet = ps.executeQuery();
                    while (resultSet.next()){
                        if (resultSet.getString("LastCounted") != null) {
                            name = resultSet.getString("LastCounted");
                        }else {
                            break;
                        }
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                if (name != null) {
                        if (name.equals(e.getAuthor().getId())) {
                            resetCount("**send a number two times in a row**", e.getMessage(), e.getMember().getId());
                            return;
                        }
                }

                if (numder == count+1){
                    e.getMessage().addReaction(Emoji.fromFormatted("\u2705")).queue();
                    count++;

                    PreparedStatement preparedStatement;
                    try {
                        preparedStatement = main.getDatabase().getConnection().prepareStatement("UPDATE DiscordInfo SET LastCounted = ? WHERE InfoType = ?");
                        preparedStatement.setString(1, e.getMember().getUser().getId());
                        preparedStatement.setString(2, "Count");
                        preparedStatement.executeUpdate();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                    if (corretCounded.containsKey(e.getMember().getId())){
                        Integer amount = corretCounded.get(e.getMember().getId());
                        corretCounded.put(e.getMember().getId(), amount+1);
                    }else {
                        Integer amount = 0;
                        try {
                            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT CorrectlyCounded FROM DiscordUserInfo WHERE DiscordID = ?");
                            ps.setString(1, e.getMember().getId());
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                                if (rs.getString("CorrectlyCounded") != null) {
                                    amount = Integer.valueOf(rs.getString("CorrectlyCounded"));
                                }
                            }
                            corretCounded.put(e.getMember().getId(), amount+1);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }

                    }

                    if (count >= CountHighsource){
                        CountHighsource = count;
                        ID = e.getMember().getUser().getName();
                    }

                }else {
                    resetCount("**Wrong Number**", e.getMessage(), e.getMember().getId());
                }

            }catch (NumberFormatException e1){

            }

            }
    }
    private void resetCount(String reason, Message message, String ID){

        count = 0;

        try {
            PreparedStatement ps1 = main.getDatabase().getConnection().prepareStatement("UPDATE DiscordInfo SET CountNumder = ? WHERE InfoType = ?");
            ps1.setString(1, "0");
            ps1.setString(2, "Count");
            ps1.executeUpdate();

            PreparedStatement ps2 = main.getDatabase().getConnection().prepareStatement("UPDATE DiscordInfo SET LastCounted = ? WHERE InfoType = ?");
            ps2.setString(1, null);
            ps2.setString(2, "Count");
            ps2.executeUpdate();

            EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.RED).setAuthor("TheRift")
                    .setTitle("Count has been reset")
                    .setDescription(message.getAuthor().getAsMention() + " just " + reason)
                    .setFooter("The count has started at 0");

            message.replyEmbeds(embedBuilder.build()).queue();
            message.addReaction(Emoji.fromUnicode("U+274C")).queue();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (wrongCounded.containsKey(ID)){
            Integer cound = wrongCounded.get(ID);
            wrongCounded.put(ID, cound+1);
        }else {
            Integer amount = 0;
            try {
                PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT WronglyCounded FROM DiscordUserInfo WHERE DiscordID = ?");
                ps.setString(1, ID);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getString("WronglyCounded") != null) {
                        amount = Integer.valueOf(rs.getString("WronglyCounded"));
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            wrongCounded.put(ID, amount+1);
        }

    }
    public void onDis(){
        try {
            PreparedStatement ps1 = main.getDatabase().getConnection().prepareStatement("UPDATE DiscordInfo SET CountNumder = ? WHERE InfoType = ?");
            ps1.setString(1, String.valueOf(count));
            ps1.setString(2, "Count");
            ps1.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        try {
            PreparedStatement ps12 = main.getDatabase().getConnection().prepareStatement("UPDATE DiscordInfo SET CountNumder = ? WHERE InfoType = ?");
            ps12.setString(1, String.valueOf(CountHighsource));
            ps12.setString(2, "HighScore");
            ps12.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        try {
            PreparedStatement ps12 = main.getDatabase().getConnection().prepareStatement("UPDATE DiscordInfo SET LastCounted = ? WHERE InfoType = ?");
            ps12.setString(1, ID);
            ps12.setString(2, "HighScore");
            ps12.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
    public void onSave(){
        if (!wrongCounded.isEmpty()){
            for (String ID : wrongCounded.keySet()){
                Integer amount = wrongCounded.get(ID);
                try {
                    PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT * FROM DiscordUserInfo WHERE DiscordID = ?");
                    ps.setString(1, ID);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()){
                        PreparedStatement ps1 = main.getDatabase().getConnection().prepareStatement("UPDATE DiscordUserInfo SET WronglyCounded = ? WHERE DiscordID = ?");
                        ps1.setString(1, amount.toString());
                        ps1.setString(2, ID);
                        ps1.executeUpdate();
                    }else {
                        PreparedStatement ps2 = main.getDatabase().getConnection().prepareStatement("INSERT INTO DiscordUserInfo (DiscordID, WronglyCounded) VALUES (?,?)");
                        ps2.setString(1, ID);
                        ps2.setString(2, amount.toString());
                        ps2.executeUpdate();
                    }

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                wrongCounded.remove(ID);
            }
        }
        if (!corretCounded.isEmpty()){
            for (String ID : corretCounded.keySet()){
                Integer amount = corretCounded.get(ID);
                try {
                    PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT * FROM DiscordUserInfo WHERE DiscordID = ?");
                    ps.setString(1, ID);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()){
                        PreparedStatement ps1 = main.getDatabase().getConnection().prepareStatement("UPDATE DiscordUserInfo SET CorrectlyCounded = ? WHERE DiscordID = ?");
                        ps1.setString(1, amount.toString());
                        ps1.setString(2, ID);
                        ps1.executeUpdate();
                    }else {
                        PreparedStatement ps2 = main.getDatabase().getConnection().prepareStatement("INSERT INTO DiscordUserInfo (DiscordID, CorrectlyCounded) VALUES (?,?)");
                        ps2.setString(1, ID);
                        ps2.setString(2, amount.toString());
                        ps2.executeUpdate();
                    }

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                corretCounded.remove(ID);
            }
        }
    }

    public HashMap<String, Integer> getCorretCounded() {
        return corretCounded;
    }

    public HashMap<String, Integer> getWrongCounded() {
        return wrongCounded;
    }
}
