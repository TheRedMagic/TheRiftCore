package com.therift.theriftcore.Discord.Commands.StaffCommands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.bukkit.Bukkit;

import java.awt.*;

public class KickCommandDiscord {
    public void command(Guild guild){
        guild.upsertCommand("kick", "Kick a user")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.KICK_MEMBERS))
                .addOption(OptionType.USER, "user", "User you what to kick")
                .addOption(OptionType.STRING, "reason", "Reason for kick")
                .queue();
    }
    public void command(SlashCommandInteractionEvent e){
        if (Bukkit.getWorld("Spawn") != null){
            if (e.getName().equals("kick")){
                User user = e.getOption("user").getAsUser();
                String reason = e.getOption("reason").getAsString();

                e.getGuild().kick(user, reason).queue();

                TextChannel textChannel1 = e.getGuild().getTextChannelById("1012486403462012938");
                EmbedBuilder embedBuilder1 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                        .setTitle("Discord Kick")
                        .setDescription(user.getName() + " has been kicked form the discord");
                textChannel1.sendMessageEmbeds(embedBuilder1.build()).queue();

                e.deferReply(true).queue();
                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                        .setTitle("Kick")
                        .setDescription(user.getName() + " has been kicked");
                e.getHook().sendMessageEmbeds(embedBuilder.build()).queue();

                EmbedBuilder builder = new EmbedBuilder().setColor(Color.RED).setAuthor("TheRift")
                        .setTitle("Kick")
                        .setDescription("You are kicked by " + user.getName());

                user.openPrivateChannel().complete()
                        .sendMessageEmbeds(builder.build()).queue();



            }
        }
    }
}
