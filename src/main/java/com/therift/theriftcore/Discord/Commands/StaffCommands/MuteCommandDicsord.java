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
import java.util.concurrent.TimeUnit;

public class MuteCommandDicsord {
    private TheRiftCore main;

    public void command(Guild guild, TheRiftCore main){
        this.main = main;
        guild.upsertCommand("mute", "Mutes player")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_MUTE_OTHERS))
                .addOption(OptionType.USER, "user", "User you what to timeout", true)
                .addOption(OptionType.INTEGER, "time", "The amount of time", true)
                .addOption(OptionType.STRING, "reason", "Gives reason for mute", true)
                .queue();
    }

    public void mute(SlashCommandInteractionEvent e){
        if(Bukkit.getWorld("Spawn") != null){
            if (e.getName().equals("mute")){
                User user = e.getOption("user").getAsUser();
                Integer time = e.getOption("time").getAsInt();

                e.getGuild().timeoutFor(user, time, TimeUnit.HOURS).queue();

                main.getPlayerManager().addDiscordPunish(user.getId(), user.getName(), "Mute", e.getOption("reason").getAsString());

                TextChannel textChannel1 = e.getGuild().getTextChannelById("1012486403462012938");
                EmbedBuilder embedBuilder1 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                        .setTitle("Discord Mute")
                                .setDescription(user.getName() + " has been muted in discord by " + e.getUser().getName() + "\nReason : " + e.getOption("reason").getAsString());
                textChannel1.sendMessageEmbeds(embedBuilder1.build()).queue();

                e.deferReply(true).queue();
                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                        .setTitle("Mute")
                        .setDescription(user.getName() + " has been muted");
                e.getHook().sendMessageEmbeds(embedBuilder.build()).queue();

                EmbedBuilder builder = new EmbedBuilder().setColor(Color.RED).setAuthor("TheRift")
                        .setTitle("Mute")
                        .setDescription("You are muted by " + user.getName() + "\nReason : " + e.getOption("reason").getAsString());

                user.openPrivateChannel().complete()
                        .sendMessageEmbeds(builder.build()).queue();
            }
        }
    }
}
