package com.therift.theriftcore.Discord.DiscordStaff;

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

public class BanCommandDisocrd {
    public void command(Guild guild){
        guild.upsertCommand("ban", "Bans user for the discord")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
                .addOption(OptionType.USER, "user", "User you what to ban")
                .addOption(OptionType.STRING, "reason", "Reason for ban")
                .queue();
    }

    public void ban(SlashCommandInteractionEvent e){
        if (Bukkit.getWorld("Spawn") != null){
            if (e.getName().equals("ban")){
                User user = e.getOption("user").getAsUser();
                String reason = e.getOption("reason").getAsString();

                e.getGuild().ban(user, 0, reason).queue();


                TextChannel textChannel1 = e.getGuild().getTextChannelById("1012486403462012938");
                EmbedBuilder embedBuilder1 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                        .setTitle("Discord Ban")
                        .setDescription(user.getName() + " has been banned form the discord");
                textChannel1.sendMessageEmbeds(embedBuilder1.build()).queue();

                e.deferReply(true).queue();
                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                        .setTitle("Ban")
                        .setDescription(user.getName() + " has been banned");
                e.getHook().sendMessageEmbeds(embedBuilder.build()).queue();

                EmbedBuilder builder = new EmbedBuilder().setColor(Color.RED).setAuthor("TheRift")
                        .setTitle("Banned")
                        .setDescription("You are banned by " + user.getName());

                user.openPrivateChannel().complete()
                        .sendMessageEmbeds(builder.build()).queue();



            }
        }
    }
}
