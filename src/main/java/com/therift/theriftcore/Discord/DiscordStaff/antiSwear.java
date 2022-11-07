package com.therift.theriftcore.Discord.DiscordStaff;

import com.therift.theriftcore.TheRiftCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Calendar;

public class antiSwear {
    private TheRiftCore main;
    private boolean deleteword = false;

    public antiSwear(TheRiftCore main){
        this.main = main;
    }
    public void antiSwear(@NotNull MessageReceivedEvent e){
        if (Bukkit.getWorld("Spawn") != null){
            if (e.getMessage() != null){
                deleteword = false;


                deleteWords(e);
                if (!deleteword) {
                    badword(e);
                }

            }
        }
    }

    private void deleteWords(MessageReceivedEvent e){
        Member member = e.getMember();
        Message message = e.getMessage();
        User user = e.getAuthor();
        String normalMessage = e.getMessage().getContentRaw();
        String Message_NoSpace = normalMessage.replace(" ", "").toLowerCase();

        TextChannel PunishChannel = e.getGuild().getTextChannelById("1030117474139656192");

        for (String s : main.getConfig().getStringList("Delete-Words")){
            if (Message_NoSpace.contains(s)){
                message.delete().queue();

                main.getPlayerManager().addDiscordPunish(user.getId(), user.getName(), "AntiSwear", normalMessage);

                Calendar calendar = Calendar.getInstance();

                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.RED)
                        .setAuthor("TheRift")
                        .setTitle("Deleted Word")
                        .setDescription("\n**Info**\nUser: " + member.getAsMention() + "\nTime : " + calendar.getTime() + "\n\n**Message**\n" + normalMessage);
                PunishChannel.sendMessageEmbeds(embedBuilder.build()).queue();

                EmbedBuilder embedBuilder1 = new EmbedBuilder().setColor(Color.RED)
                        .setAuthor("TheRift")
                        .setTitle("AntiSwear")
                        .setDescription("**Info**\nPlease do not use that language in The Rift discord server again!")
                        .setFooter("This is a automated message this can be wrong. If this a un rightfully done please report to a staff mender");

                user.openPrivateChannel().complete()
                        .sendMessageEmbeds(embedBuilder1.build()).queue();

                deleteword = true;
                break;
            }
        }
    }

    private void badword(MessageReceivedEvent e) {

        Member member = e.getMember();
        Message message = e.getMessage();
        User user = e.getAuthor();
        String normalMessage = e.getMessage().getContentRaw();
        String Message_NoSpace = normalMessage.toLowerCase();

        TextChannel PunishChannel = e.getGuild().getTextChannelById("1030117474139656192");

        for (String s : main.getConfig().getStringList("Bad-word")) {
            if (Message_NoSpace.contains(s)) {
                message.delete().queue();

                main.getPlayerManager().addDiscordPunish(user.getId(), user.getName(), "AntiSwear", normalMessage);

                Calendar calendar = Calendar.getInstance();

                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setAuthor("TheRift")
                        .setTitle("AntiSwear")
                        .setDescription(member.getAsMention() + " just send a message and was flagged by the **AntiSwear**. The Message is below un hide at your own risk\n\n**Message**\n||" + normalMessage + "||")
                        .setFooter("A staff mender will review this as fast as possible");

                e.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message1 -> {


                    PunishChannel.sendMessage(e.getGuild().getRoleById("1002216542504755240").getAsMention() + "\nPlease check this message as fast as you can");
                    EmbedBuilder embedBuilder1 = new EmbedBuilder().setColor(Color.RED)
                            .setAuthor("TheRift")
                            .setTitle("Bad Word")
                            .setDescription("\n**Info**\nUser: " + member.getAsMention() + "\nTime : " + calendar.getTime() + "\n\n**Message**\n" + normalMessage)
                            .setFooter("Message ID: " + message1.getId() + "\nTextChannel ID: " + message1.getChannel().getId());
                    Button deleteButton = Button.danger("delete", "Delete");
                    PunishChannel.sendMessageEmbeds(embedBuilder1.build()).setActionRows(ActionRow.of(deleteButton)).queue();
                });
            }
        }
    }

    public void onButton(ButtonInteractionEvent e){
        if (e.getComponentId().equals("delete")){
            MessageEmbed messageEmbed = e.getMessage().getEmbeds().get(0);

            String[] a = messageEmbed.getFooter().getText().split(" ");

            String MessageID = a[2].replace("\nTextChannel", "");
            String TextChannelID = a[a.length - 1];

            TextChannel textChannel = e.getGuild().getTextChannelById(TextChannelID);


            if (textChannel.retrieveMessageById(MessageID) != null) {
                textChannel.retrieveMessageById(MessageID).queue(message -> {
                    if (message != null) {
                        message.delete().queue();
                    }
                });
            }else {
                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.RED)
                        .setAuthor("TheRift")
                        .setTitle("AntiSwear")
                        .setDescription("Message has already been deleted");
                e.deferReply(true).queue();
                e.getInteraction().getHook().sendMessageEmbeds(embedBuilder.build()).queue();
            }

            EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.RED)
                    .setAuthor("TheRift")
                    .setTitle("AntiSwear")
                    .setDescription("Message Deleted");
            e.deferReply(true).queue();
            e.getInteraction().getHook().sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }
}
