package com.therift.theriftcore.Discord.DiscordUntils;

import com.therift.theriftcore.TheRiftCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenuInteraction;
import org.bukkit.Bukkit;

import javax.swing.plaf.basic.BasicTableHeaderUI;
import java.awt.*;

public class Roles {
    private TheRiftCore main;
    public Roles(TheRiftCore main){
        this.main = main;
    }

    public void onCommand(Guild guild){
        guild.upsertCommand("roles", "Opens a new roles text")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_PERMISSIONS))
                .queue();
    }

    public void channelRoles(SlashCommandInteractionEvent e){
        if (!e.getName().equals("roles")) return;
        TextChannel textChannel = e.getGuild().getTextChannelById(main.getConfig().getString("RolesID"));

        SelectMenu selectMenu = SelectMenu.create("menu:role")
                .setPlaceholder("Choose your roles")
                .addOption("Updates \uD83D\uDD3C", "updates")
                .addOption("Polls \uD83D\uDCCA", "polls")
                .addOption("Social \uD83D\uDCF9", "social")
                .build();

        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Roles").setDescription("Collect your roles");

        textChannel.sendMessageEmbeds(embedBuilder.build())
                .setActionRow(selectMenu)
                .queue();

        e.deferReply(true).queue();
        e.getHook().sendMessage("Done").queue();

    }

    public void onSelectionMenu(SelectMenuInteractionEvent e){
        String label = e.getSelectedOptions().get(0).getLabel();
        Member member = e.getMember();
        Guild guild = e.getGuild();
        System.out.println(label);
        if (label.equals("Updates \uD83D\uDD3C")){
            guild.addRoleToMember(member, e.getJDA().getRoleById("1011263700134535318")).complete();
            EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Role Given");
            e.replyEmbeds(embedBuilder.build())
                    .setEphemeral(true)
                    .queue();
        }
        if (label.equals("Polls \uD83D\uDCCA")){
            guild.addRoleToMember(e.getMember(), e.getJDA().getRoleById("1022156744543186964")).complete();
            EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Role Given");
            e.replyEmbeds(embedBuilder.build())
                    .setEphemeral(true)
                    .queue();
        }
        if (label.equals("Social \uD83D\uDCF9")){
            guild.addRoleToMember(e.getMember(), e.getJDA().getRoleById("1034575284554444830")).complete();
            EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Role Given");
            e.replyEmbeds(embedBuilder.build())
                    .setEphemeral(true)
                    .queue();
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
