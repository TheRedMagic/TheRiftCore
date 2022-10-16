package com.therift.theriftcore.Discord.Commands.StaffCommands;

import com.therift.theriftcore.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bukkit.Bukkit;

import java.awt.*;
import java.time.Instant;

public class PollCommand {

    private Main main;

    public PollCommand(Main main){
        this.main = main;
    }

    public void command(Guild guild){
        guild.upsertCommand("poll", "Create a Poll")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MENTION_EVERYONE))
                .addOption(OptionType.STRING, "about", "What the poll is about", true)
                .queue();
    }

    public void info(SlashCommandInteractionEvent event){
        if (Bukkit.getWorld("Spawn") != null) {
            if (event.getName().equals("poll")) {
                    EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                            .setTitle("Poll")
                            .setDescription("Made by : " + event.getUser().getName() + "\n" + event.getOption("about").getAsString() + "\n\n || " + event.getGuild().getRoleById("1022156744543186964").getAsMention() + "||");
                    TextChannel textChannel = event.getGuild().getTextChannelById("1022164341065252924");


                    textChannel.sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                        message.addReaction(Emoji.fromUnicode("\u2705")).queue();
                        message.addReaction(Emoji.fromUnicode("\u274E")).queue();
                    });

                event.deferReply(true).queue();
                MessageEmbed embedBuilder1 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                        .setTitle("Poll")
                        .setDescription("Poll Created")
                        .build();

                event.getHook().sendMessageEmbeds(embedBuilder1).queue();
            }
        }
    }
}
