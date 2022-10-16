package com.therift.theriftcore.Discord.Commands.StaffCommands;

import com.therift.theriftcore.Main;
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
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MuteCommandDicsord {

    public void command(Guild guild){
        guild.upsertCommand("mute", "Mutes player")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_MUTE_OTHERS))
                .addOption(OptionType.USER, "user", "User you what to timeout")
                .addOption(OptionType.INTEGER, "time", "The amount of time")
                .queue();
    }

    public void mute(SlashCommandInteractionEvent e){
        if(Bukkit.getWorld("Spawn") != null){
            if (e.getName().equals("mute")){
                User user = e.getOption("user").getAsUser();
                Integer time = e.getOption("time").getAsInt();

                e.getGuild().timeoutFor(user, time, TimeUnit.HOURS).queue();

                TextChannel textChannel1 = e.getGuild().getTextChannelById("1012486403462012938");
                EmbedBuilder embedBuilder1 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                        .setTitle("Discord Mute")
                                .setDescription(user.getName() + " has been muted in discord");
                textChannel1.sendMessageEmbeds(embedBuilder1.build()).queue();

                e.deferReply(true).queue();
                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                        .setTitle("Mute")
                        .setDescription(user.getName() + " has been muted");
                e.getHook().sendMessageEmbeds(embedBuilder.build()).queue();


            }
        }
    }
}
