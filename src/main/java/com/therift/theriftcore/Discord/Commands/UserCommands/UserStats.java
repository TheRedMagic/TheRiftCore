package com.therift.theriftcore.Discord.Commands.UserCommands;

import com.therift.theriftcore.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserStats {
    Main main;
    public UserStats(Main main){
        this.main = main;
    }
    public void command(Guild guild){
        guild.upsertCommand("user-stats", "Gets the discord users stats")
                .addOption(OptionType.USER, "user", "The user you want stats about", true).queue();
    }

    public void onSlash(SlashCommandInteractionEvent e){
        if (e.getName().equals("user-stats")){
            TextChannel textChannel = e.getGuild().getTextChannelById("1009859636209385533");
            if (e.getChannel().equals(textChannel)){
                User user = e.getOption("user").getAsUser();
                Integer AmountMessagesSend = 0;
                Integer CorretlyCounded = 0;
                Integer WronglyCounded = 0;

                if (main.amountSend.getAmount().containsKey(user.getId())){
                    AmountMessagesSend = main.amountSend.getAmount().get(user.getId());
                }else {
                    try {
                        PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT MessagesSend FROM DiscordUserInfo WHERE DiscordID = ?");
                        ps.setString(1, user.getId());
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()){
                            AmountMessagesSend = Integer.valueOf(rs.getString("MessagesSend"));
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                if (main.getDiscordCounter().getCorretCounded().containsKey(user.getId())){
                    CorretlyCounded = main.getDiscordCounter().getCorretCounded().get(user.getId());
                }else {
                    try {
                        PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT CorrectlyCounded FROM DiscordUserInfo WHERE DiscordID = ?");
                        ps.setString(1, user.getId());
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            if (rs.getString("CorrectlyCounded") != null) {
                                CorretlyCounded = Integer.valueOf(rs.getString("CorrectlyCounded"));
                            }
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (main.getDiscordCounter().getWrongCounded().containsKey(user.getId())){
                    WronglyCounded = main.getDiscordCounter().getWrongCounded().get(user.getId());
                }else {
                    try {
                        PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT WronglyCounded FROM DiscordUserInfo WHERE DiscordID = ?");
                        ps.setString(1, user.getId());
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            if (rs.getString("WronglyCounded") != null) {
                                WronglyCounded = Integer.valueOf(rs.getString("WronglyCounded"));
                            }
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.GREEN).setAuthor("TheRift")
                        .setThumbnail(user.getEffectiveAvatarUrl())
                        .setTitle(user.getName() + " stats")
                        .setDescription("\nAmount Send : ``" + AmountMessagesSend +
                                "``\n\n**Counting stats**\n" +
                                "Correctly Counted : ``" + CorretlyCounded + "``" +
                                "\nWrongly Counted : ``" + WronglyCounded + "``");

                e.deferReply(true).queue();
                e.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
            }else {
                EmbedBuilder a = new EmbedBuilder().setColor(Color.RED).setAuthor("TheRift")
                        .setTitle("Wrong Channel")
                        .setDescription("Please use this command in #Bot-Commands");
                e.deferReply(true).queue();
                e.getHook().sendMessageEmbeds(a.build()).queue();
            }
        }
    }
}
