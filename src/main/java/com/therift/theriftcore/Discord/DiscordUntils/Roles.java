package com.therift.theriftcore.Discord.DiscordUntils;

import com.therift.theriftcore.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bukkit.Bukkit;

import java.awt.*;

public class Roles {
    private Main main;
    public Roles(Main main){
        this.main = main;
    }

    public void channelRoles(Guild guild){
        TextChannel textChannel = guild.getTextChannelById(main.getConfig().getString("RolesID"));
        if (textChannel.getLatestMessageId() == null) {
            EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Roles").setDescription("Collect your roles");
            net.dv8tion.jda.api.interactions.components.buttons.Button button = Button.success("update", "Updates \uD83D\uDD3C");
            Button button1 = Button.success("polls", "Polls \uD83D\uDCCA");
            Button button2 = Button.success("social", "Social \uD83D\uDCF9");
            textChannel.sendMessageEmbeds(embedBuilder.build()).setActionRows(ActionRow.of(button), ActionRow.of(button1), ActionRow.of(button2)).queue();
        }

    }

    public void ButtonVerify(ButtonInteractionEvent event){
        if (Bukkit.getWorld("Spawn") != null) {
            Guild guild = event.getGuild();
            if (event.getComponentId().equals("update")) {
                guild.addRoleToMember(event.getMember(), event.getJDA().getRoleById("1011263700134535318")).complete();
                event.deferReply(true).queue();
                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Role Given");
                event.getInteraction().getHook().sendMessageEmbeds(embedBuilder.build()).queue();
            }
            if (event.getComponentId().equals("polls")){
                guild.addRoleToMember(event.getMember(), event.getJDA().getRoleById("1022156744543186964")).complete();
                event.deferReply(true).queue();
                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Role Given");
                event.getInteraction().getHook().sendMessageEmbeds(embedBuilder.build()).queue();
            }
            if (event.getComponentId().equals("social")){
                guild.addRoleToMember(event.getMember(), event.getJDA().getRoleById("1034575284554444830")).complete();
                event.deferReply(true).queue();
                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Role Given");
                event.getInteraction().getHook().sendMessageEmbeds(embedBuilder.build()).queue();
            }
        }
    }
}
