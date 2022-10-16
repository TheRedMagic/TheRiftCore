package com.therift.theriftcore.Discord.Commands.UserCommands;

import com.therift.theriftcore.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DiscordVerifyCommand {


    private Main main;
    public DiscordVerifyCommand(Main main){
        this.main = main;
    }
    private HashMap<String, String> stringHashMap = new HashMap<>();
    private List<String> a = new ArrayList<>();
    private Date date;

    public void VerifyReadyCommand(Guild guild){
        guild.upsertCommand("verify", "Link/Verify your minecraft account")
                .addOption(OptionType.STRING, "username", "Minecraft username", true)
                .addOption(OptionType.INTEGER, "code", "Your verify code from minecraft", true)
                .queue();
    }

    public List<String> VerifyCommand(SlashCommandInteractionEvent event){
        if (event.getName().equals("verify")){
            if (event.getChannel().getId().equals(main.getConfig().getString("Verify-Channel-ID"))){
                String server = main.getPlayerManager().getServer(event.getOption("username").getAsString());
                if (server.equals("Spawn")) {
                    event.deferReply().queue();
                    if (VerifyCommand.VerifyCode.containsKey(event.getOption("username").getAsString())) {
                        if (VerifyCommand.VerifyCode.get(event.getOption("username").getAsString()).equals(event.getOption("code").getAsInt())) {
                            date = main.getPlayerManager().getPlayerJoinDate(Bukkit.getPlayer(event.getOption("username").getAsString()).getUniqueId().toString());


                            EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("This your account?").setDescription("Username : " + event.getOption("username").getAsString() + "\nFirst join date : " + date);
                            event.getHook().sendMessageEmbeds(embedBuilder.build()).addActionRow(Button.success("Yes", "Yes"), Button.danger("No", "No")).queue();
                            stringHashMap.put(event.getMember().getId(), event.getOption("username").getAsString());
                            a.add(event.getMember().getId());
                            return a;
                        } else {
                            MessageEmbed embedBuilder = new EmbedBuilder().setColor(Color.RED).setAuthor("TheRift").setTitle("Can't Find Code").setDescription("Try again").build();
                            event.getHook().sendMessageEmbeds(embedBuilder).queue();
                            return null;
                        }
                    } else {
                        MessageEmbed embedBuilder = new EmbedBuilder().setColor(Color.RED).setAuthor("TheRift").setTitle("Can't Find Player").setDescription("Try again").build();
                        event.getHook().sendMessageEmbeds(embedBuilder).queue();
                        return null;
                    }
                }else {
                    return null;
                }
            } else {
                MessageEmbed embedBuilder = new EmbedBuilder().setColor(Color.RED).setAuthor("TheRift").setTitle("Can only use this command in #verify").build();
                event.getHook().sendMessageEmbeds(embedBuilder).queue();
                return null;
            }
        }
        return null;
    }


    public void ButtonVerify(ButtonInteractionEvent event, List<String> id){
        if (event.getChannel().getId().equals(main.getConfig().getString("Verify-Channel-ID"))) {
            if (id.contains(event.getMember().getId()) && stringHashMap.containsKey(event.getMember().getId())) {
                event.deferReply().queue();
                if (event.getComponentId().equals("Yes")) {
                    if (Bukkit.getPlayer(stringHashMap.get(event.getMember().getId())) != null) {
                        try {
                            Player target = Bukkit.getPlayer(stringHashMap.get(event.getMember().getId()));
                            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET DISCORDID = ? WHERE UUID = ?");
                            ps.setString(1, event.getMember().getId());
                            ps.setString(2, String.valueOf(target.getUniqueId()));
                            ps.executeUpdate();

                            PreparedStatement ps1 = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET DISCORDNAME = ? WHERE UUID = ?");
                            ps1.setString(1, event.getMember().getUser().getName());
                            ps1.setString(2, String.valueOf(target.getUniqueId()));
                            ps1.executeUpdate();
                            Guild guild = event.getGuild();
                            guild.addRoleToMember(event.getMember(), event.getJDA().getRoleById("1009864792443453530")).complete();
                            target.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n\n" +
                                     ChatColor.RESET + ChatColor.GOLD + "\nDiscord account linked!" + ChatColor.GRAY.toString() + ChatColor.BOLD +
                                    "\n\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

                            VerifyCommand.VerifyCode.remove(target.getDisplayName());
                            MessageEmbed embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Account Link").build();
                            event.getHook().sendMessageEmbeds(embedBuilder).queue();
                            a.remove(event.getMember().getId());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        MessageEmbed embedBuilder = new EmbedBuilder().setColor(Color.RED).setAuthor("TheRift").setTitle("Can't find player").setDescription("Try again").build();
                        event.getHook().sendMessageEmbeds(embedBuilder).queue();
                    }
                } else if (event.getComponentId().equals("No")) {
                    MessageEmbed embedBuilder = new EmbedBuilder().setColor(Color.RED).setAuthor("TheRift").setTitle("Canceled minecraft account link").build();
                    event.getHook().sendMessageEmbeds(embedBuilder).queue();
                }
            }
        }
    }
}
