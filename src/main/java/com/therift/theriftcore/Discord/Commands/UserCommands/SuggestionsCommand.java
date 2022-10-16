package com.therift.theriftcore.Discord.Commands.UserCommands;

import com.therift.theriftcore.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.util.List;

public class SuggestionsCommand {

    private Main main;
    public SuggestionsCommand(Main main){
        this.main = main;
    }

    public void command(Guild guild){
        guild.upsertCommand("suggestion", "Make a suggestion where poeple can vote on")
                .addOption(OptionType.STRING, "suggest", "Suggest a suggestion", true)
                .queue();
    }

    public void info(SlashCommandInteractionEvent event){
        if (event.getName().equals("suggestion")){

            List<String> a = main.getConfig().getStringList("Bad-word");
            String suggestion = event.getOption("suggest").getAsString().replace(" ", "");

            for (String s : a){
                if (suggestion.toLowerCase().contains(s)){
                    TextChannel textChannel1 = event.getGuild().getTextChannelById(main.getConfig().getString("PunishLogId"));
                    EmbedBuilder embedBuilder12 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle(event.getUser().getName() + "\nJust send a bad word in suggestions\n||" + event.getOption("suggest").getAsString() + "||");
                    textChannel1.sendMessageEmbeds(embedBuilder12.build()).queue();


                    event.deferReply(true).queue();
                    MessageEmbed embedBuilder1 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                            .setTitle(event.getUser().getName() + "\nPlease don't use that language in this discord server")
                            .build();

                    event.getHook().sendMessageEmbeds(embedBuilder1).queue();



                    return;
                }
            }



            EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                    .setTitle("Suggestion")
                    .setDescription("**Made by : " + event.getUser().getName() + "**\n\n" + event.getOption("suggest").getAsString());
            TextChannel textChannel = event.getGuild().getTextChannelById("1022206485285044387");




            textChannel.sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                message.addReaction(Emoji.fromUnicode("\u2705")).queue();
                message.addReaction(Emoji.fromUnicode("\u274E")).queue();
            });

            event.deferReply(true).queue();
            MessageEmbed embedBuilder1 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                    .setTitle("Suggestion")
                    .setDescription("Suggestion Created")
                    .build();

            event.getHook().sendMessageEmbeds(embedBuilder1).queue();
        }
    }
}
