package com.therift.theriftcore.Discord.Commands.UserCommands;

import com.therift.theriftcore.Discord.DiscordListener;
import com.therift.theriftcore.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HighScoreMessager {

    private Main main;

    public HighScoreMessager(Main main){
        this.main = main;
    }

    public void command(Guild guild){
        guild.upsertCommand("top-messages", "Gets the top 10 most send messages").queue();
    }

    public void onSlash(SlashCommandInteractionEvent e){
        if(e.getName().equals("top-messages")){
            try {
                int times = 1;
                PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT * FROM DiscordUserInfo ORDER BY MessagesSend DESC");
                ResultSet rs = ps.executeQuery();

                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.GREEN).setAuthor("TheRift")
                        .setTitle("Top 10")
                        .setDescription("Top 10 most send users:");

                while (rs.next() && times != 11){

                    System.out.println("Looping");

                    String ID = rs.getString("DiscordID");
                    Integer Amount = rs.getInt("MessagesSend");
                    Integer finalTimes = times;

                    User user = DiscordListener.jda.retrieveUserById(ID).complete();
                    System.out.println(ID + "|" + Amount + "|" + user.getName());

                    embedBuilder.getDescriptionBuilder().append("\n" + finalTimes +". "  + user.getName() + " : ``" + Amount + "``");

                    times++;
                }

                e.deferReply().queue();
                e.getHook().sendMessageEmbeds(embedBuilder.build()).complete();

            }catch (SQLException e2){
                throw new RuntimeException(e2);
            }
        }
    }
}
