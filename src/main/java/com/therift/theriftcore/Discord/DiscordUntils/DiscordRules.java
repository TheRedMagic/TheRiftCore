package com.therift.theriftcore.Discord.DiscordUntils;

import com.therift.theriftcore.TheRiftCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class DiscordRules {
    private TheRiftCore main;
    public DiscordRules(TheRiftCore main){
        this.main = main;
    }
    public void Rules(Guild guild){
        TextChannel textChannel = guild.getTextChannelById(main.getConfig().getString("RulesChannel"));
        if (textChannel.getLatestMessageId() == null) {
            EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Rules").setDescription("**Discord Server Rules**\n" +
                    "- Do not be rude to others. You can make jokes but there is a fine line between banter and being mean.\n\n" +
                    "-Discriminatory actions/behaviors are highly prohibited.\n\n" +
                    "-No spamming, slurs, trying to bypass blacklisted words, and/or ping spamming. Make sure to keep swearing to a minimum\n\n" +
                    "-Make sure to keep controversial topics to a minimum or take it to dms.\n\n" +
                    "-Porn, gore, NSFW, explicit topics, child abuse, animal abuse, gambling or any other material that ties to it is unacceptable.\n\n" +
                    "-Use correct channels and correct chats to keep everything more organized. \n\n" +
                    "-Speak only English\n\n" +
                    "-Asking to be staff or admin will not increase your likelihood of being one. There will be applications for that.\n\n" +
                    "-This is a safe space for anyone to be themselves without judgement. Hate from anyone will result in a permanent ban. We are very serious about this.\n\n\n" +
                    "**In Game Rules**\n\n" +
                    "- Do not be rude to others. You can make jokes but there is a fine line between banter and being mean.\n\n" +
                    "-Discriminatory actions/behaviors are highly prohibited.\n\n" +
                    "-No spamming, slurs, or trying to bypass blacklisted words, Make sure to keep swearing to a minimum.\n\n" +
                    "-Make sure to keep controversial topics to a minimum.\n\n" +
                    "-Porn, gore, NSFW, explicit topics, child abuse, animal abuse, gambling or any other material that ties to it is unacceptable. \n\n" +
                    "-Asking to be staff or admin will not increase your likelihood of being one. There will be applications for that.\n\n" +
                    "-Do not cheat in any way. This includes Xray and any kind of hacked client. Doing so will result in ban.");
            textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }
}
