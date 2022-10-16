package com.therift.theriftcore.Discord;

import com.therift.theriftcore.Discord.Commands.StaffCommands.*;
import com.therift.theriftcore.Discord.Commands.UserCommands.DiscordVerifyCommand;
import com.therift.theriftcore.Discord.Commands.UserCommands.SuggestionsCommand;
import com.therift.theriftcore.Discord.DiscordStaff.BanCommandDisocrd;
import com.therift.theriftcore.Discord.DiscordStaff.antiSwear;
import com.therift.theriftcore.Discord.DiscordUntils.DiscordRules;
import com.therift.theriftcore.Discord.DiscordUntils.DiscordVerify;
import com.therift.theriftcore.Discord.DiscordUntils.Roles;
import com.therift.theriftcore.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DiscordListener extends ListenerAdapter {
    private Main main;
    public static JDA jda;
    private DiscordVerifyCommand discordVerifyCommand;
    private DiscordVerify discordVerify;
    private DiscordRules discordRules;
    private Roles roles;
    private com.therift.theriftcore.Discord.DiscordStaff.antiSwear antiSwear;
    private PollCommand pollCommand;
    private String user;
    private String Message;
    private ArrayList<String> badWords = new ArrayList<>();
    private SuggestionsCommand suggestionsCommand;
    private List<String> id = new ArrayList<>();
    private MuteCommandDicsord muteCommandDicsord;
    private KickCommandDiscord kickCommandDiscord;
    private BanCommandDisocrd banCommandDisocrd;
    private GivewayCommand givewayCommand;
    private PunishHistory punishHistory;
    public DiscordListener(Main main){
        this.main = main;
        discordVerify = new DiscordVerify(main);
        discordVerifyCommand = new DiscordVerifyCommand(main);
        discordRules = new DiscordRules(main);
        roles = new Roles(main);
        antiSwear = new antiSwear(main);
        pollCommand = new PollCommand(main);
        suggestionsCommand = new SuggestionsCommand(main);
        muteCommandDicsord = new MuteCommandDicsord();
        kickCommandDiscord = new KickCommandDiscord();
        banCommandDisocrd = new BanCommandDisocrd();
        givewayCommand = new GivewayCommand();
        punishHistory = new PunishHistory(main);
    }
    public void main()
    {
            try {
                jda = JDABuilder.createDefault(
                                main.getConfig().getString("Discord-Token"))
                        .setActivity(Activity.playing("TheRift Network"))
                        .addEventListeners(this)
                        .enableIntents(GatewayIntent.GUILD_MEMBERS)
                        .enableIntents(GatewayIntent.DIRECT_MESSAGES)
                        .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                        .enableIntents(GatewayIntent.GUILD_MESSAGES)
                        .build().awaitReady();
            } catch (LoginException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


    }

    @Override
    public void onReady(ReadyEvent event){
        Guild guild = event.getJDA().getGuildById(main.getConfig().getString("GuildID"));
        discordVerifyCommand.VerifyReadyCommand(guild);
        discordVerify.Verify(guild);
        discordRules.Rules(guild);
        roles.channelRoles(guild);
        pollCommand.command(guild);
        suggestionsCommand.command(guild);
        muteCommandDicsord.command(guild);
        kickCommandDiscord.command(guild);
        banCommandDisocrd.command(guild);
        givewayCommand.command(guild);
        punishHistory.command(guild);

        for (String s : main.getConfig().getStringList("Bad-word")){
            badWords.add(s);
        }


    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e){
        if (Bukkit.getWorld("Spawn") != null) {

            TextChannel textChannel2 = e.getGuild().getTextChannelById("1018857837268574278");
            if (!textChannel2.equals(e.getChannel()) && !textChannel2.equals(e.getGuild().getTextChannelById("1018857686428823623")) && !textChannel2.equals(e.getGuild().getTextChannelById("1012486403462012938")) && !textChannel2.equals(e.getGuild().getTextChannelById("1012690050166829086"))) {
                if (!e.getMember().getUser().getId().equals("1008045194375078031")) {
                    EmbedBuilder embedBuilder2 = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle(e.getMember().getUser().getAsMention()).setDescription("Sent : " + e.getMessage().getContentRaw() + "\nChannel : " + e.getChannel().getName());
                    textChannel2.sendMessageEmbeds(embedBuilder2.build()).queue();
                }
            }

            antiSwear.antiSwear(e);
        }
    }
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        if (Bukkit.getWorld("Spawn") != null) {
            id = discordVerifyCommand.VerifyCommand(event);
            pollCommand.info(event);
            suggestionsCommand.info(event);
            muteCommandDicsord.mute(event);
            kickCommandDiscord.command(event);
            banCommandDisocrd.ban(event);
            givewayCommand.giveaway(event);
            punishHistory.punishHistory(event);
        }
    }
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event){
        if (Bukkit.getWorld("Spawn") != null) {
            antiSwear.onButton(event);
            discordVerifyCommand.ButtonVerify(event, id);
            discordVerify.ButtonVerify(event);
            roles.ButtonVerify(event);
        }
    }
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event){
        if (Bukkit.getWorld("Spawn") != null) {
            EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift")
                    .setTitle("Welcome " + event.getMember().getUser().getName() + " to TheRift")
                    .setDescription("This is the discord server for: \n**TheRift Minecraft server**\nPlease read #server-rules")
                    .setThumbnail(event.getMember().getUser().getEffectiveAvatarUrl())
                    .setTimestamp(Instant.now());
            TextChannel textChannel = event.getGuild().getTextChannelById(main.getConfig().getString("welcome-Channel"));
            textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }
}
