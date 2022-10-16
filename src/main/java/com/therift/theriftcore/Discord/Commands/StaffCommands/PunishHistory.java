package com.therift.theriftcore.Discord.Commands.StaffCommands;

import com.therift.theriftcore.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.bukkit.Bukkit;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PunishHistory {
    private Main main;

    public PunishHistory(Main main){
        this.main = main;
    }

    public void command(Guild guild){
        guild.upsertCommand("punish-history", "Gets the punish history for the player")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_MUTE_OTHERS))
                .addOption(OptionType.USER, "user", "The user to get the punish history", true)
                .queue();
    }

    public void punishHistory(SlashCommandInteractionEvent e){
        if (Bukkit.getWorld("Spawn") != null){
            if (e.getName().equals("punish-history")){
                try {
                    ResultSet rs = main.getPlayerManager().getDiscordPunishHistory(e.getOption("user").getAsUser().getId());
                    EmbedBuilder embedBuilder = new EmbedBuilder()
                            .setColor(Color.RED)
                            .setAuthor("TheRift")
                            .setTitle("Punish History")
                            .setDescription("Punish History : " + e.getOption("user").getAsUser().getAsMention() + "\n This is the discord **Punish History** from " + e.getOption("user").getAsUser().getAsMention());
                    e.deferReply().queue();
                    e.getHook().sendMessageEmbeds(embedBuilder.build()).queue();

                    while (rs.next()){
                        EmbedBuilder embedBuilder1 = new EmbedBuilder()
                                .setColor(Color.RED)
                                .setAuthor("TheRift")
                                .setTitle(e.getOption("user").getAsUser().getName() + " Punish History")
                                .setDescription("PunishType : " + rs.getString("PunishType")
                                + "\nInfo : " + rs.getString("Info"));
                        e.deferReply(true).queue();
                        e.getHook().sendMessageEmbeds(embedBuilder1.build()).queue();
                    }
                } catch (SQLException es){
                    throw  new RuntimeException(es);
                }
            }
        }
    }
}
