package com.therift.theriftcore.Discord.Commands.StaffCommands;

import com.therift.theriftcore.TheRiftCore;
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
    private TheRiftCore main;
    public void command(Guild guild, TheRiftCore main){
        this.main = main;
        guild.upsertCommand("ban", "Bans user for the discord")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
                .addOption(OptionType.USER, "user", "User you what to ban", true)
                .addOption(OptionType.STRING, "reason", "Reason for ban", true)
                .queue();
    }

    public void ban(SlashCommandInteractionEvent e){
        if (Bukkit.getWorld("Spawn") != null){
            if (e.getName().equals("ban")){
                User user = e.getOption("user").getAsUser();
                String reason = e.getOption("reason").getAsString();

                e.getGuild().ban(user, 0, reason).queue();

                main.getPlayerManager().addDiscordPunish(user.getId(), user.getName(), "Mute", e.getOption("reason").getAsString());


                TextChannel textChannel1 = e.getGuild().getTextChannelById("1012486403462012938");
                EmbedBuilder embedBuilder1 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                        .setTitle("Discord Ban")
                        .setDescription(user.getName() + " has been banned form the discord\nReason : " + reason);
                textChannel1.sendMessageEmbeds(embedBuilder1.build()).queue();

                e.deferReply(true).queue();
                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                        .setTitle("Ban")
                        .setDescription(user.getName() + " has been banned");
                e.getHook().sendMessageEmbeds(embedBuilder.build()).queue();

                EmbedBuilder builder = new EmbedBuilder().setColor(Color.RED).setAuthor("TheRift")
                        .setTitle("Banned")
                        .setDescription("You are banned by " + user.getName() + "\nReason : " + reason);

                user.openPrivateChannel().complete()
                        .sendMessageEmbeds(builder.build()).queue();



            }
        }
    }
}
