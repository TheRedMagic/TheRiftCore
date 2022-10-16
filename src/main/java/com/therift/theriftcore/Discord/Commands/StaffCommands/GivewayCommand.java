package com.therift.theriftcore.Discord.Commands.StaffCommands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.Bukkit;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class GivewayCommand {


    public void command(Guild guild){
        guild.upsertCommand("giveaway", "Create a giveaway")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MENTION_EVERYONE))
                .addOption(OptionType.STRING, "info", "Info about the giveaway", true)
                .queue();

        guild.upsertCommand("roll-giveaway", "Roll a player from the a giveaway")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MENTION_EVERYONE))
                .addOption(OptionType.STRING, "message-id", "The message id from the giveaway you what to roll", true)
                .queue();
    }

    public void giveaway(SlashCommandInteractionEvent e){
        if (Bukkit.getWorld("Spawn") != null){
            if (e.getName().equals("giveaway")){
                String info = e.getOption("info").getAsString();

                TextChannel textChannel = e.getGuild().getTextChannelById("1027157515131158589");
                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                        .setTitle("Giveaway")
                        .setDescription("\n**Giveaway Info**\n\n" + info)
                        .setFooter("Not Done");
                textChannel.sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                    message.addReaction(Emoji.fromFormatted("\u2705")).queue();
                });

                e.deferReply(true).queue();
                MessageEmbed embedBuilder1 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                        .setTitle("Giveaway")
                        .setDescription("**Giveaway Creation**\n\n" + info)
                        .build();
                e.getHook().sendMessageEmbeds(embedBuilder1).queue();
            }


            if (e.getName().equals("roll-giveaway")){
                String messageid = e.getOption("message-id").getAsString();

                TextChannel textChannel = e.getGuild().getTextChannelById("1027157515131158589");
                if (textChannel.retrieveMessageById(messageid) != null){
                    textChannel.retrieveMessageById(messageid).queue(message -> {
                        if (message.getReactions() != null){

                            MessageReaction reaction = message.getReaction(Emoji.fromUnicode("\u2705"));
                            reaction.retrieveUsers().queue(users1 -> {
                                List<User> users = new ArrayList<>();
                                users = users1;

                                if (!users.isEmpty()){
                                    Random random = new Random();

                                    User user = users.get(random.nextInt(users.size()));

                                    if (!user.isBot()){

                                    MessageEmbed embedBuilder1 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                                            .setTitle("Giveaway")
                                            .setDescription("\n**Winner**\n" + user.getAsMention())
                                            .setFooter("A staff mender will dm you shortly")
                                            .build();
                                    message.replyEmbeds(embedBuilder1).queue();

                                    e.deferReply(true).queue();
                                    MessageEmbed embedBuilder2 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                                                .setTitle("Giveaway")
                                                .setDescription("Winner has been Chosen")
                                                .build();
                                    e.getHook().sendMessageEmbeds(embedBuilder2).queue();

                                    } else {
                                        e.deferReply(true).queue();
                                        MessageEmbed embedBuilder1 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                                                .setTitle("Giveaway")
                                                .setDescription("Please reRoll")
                                                .build();
                                        e.getHook().sendMessageEmbeds(embedBuilder1).queue();;
                                    }


                                }else {
                                    e.deferReply(true).queue();
                                    MessageEmbed embedBuilder1 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                                            .setTitle("Giveaway")
                                            .setDescription("No one reacted")
                                            .build();
                                    e.getHook().sendMessageEmbeds(embedBuilder1).queue();
                                }
                            });


                        }else {
                            e.deferReply(true).queue();
                            MessageEmbed embedBuilder1 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                                    .setTitle("Giveaway")
                                    .setDescription("Message doesn't have a reaction")
                                    .build();
                            e.getHook().sendMessageEmbeds(embedBuilder1).queue();
                        }
                    });



                }else {
                    e.deferReply(true).queue();
                    MessageEmbed embedBuilder1 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                            .setTitle("Giveaway")
                            .setDescription("Can't find Message")
                            .build();
                    e.getHook().sendMessageEmbeds(embedBuilder1).queue();
                }
            }
        }
    }
}
