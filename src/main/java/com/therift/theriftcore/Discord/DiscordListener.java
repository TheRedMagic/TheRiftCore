package com.therift.theriftcore.Discord;

import com.therift.theriftcore.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiscordListener extends ListenerAdapter {
    private Main main;
    private JDA jda;
    private DiscordVerifyCommand discordVerifyCommand;
    private List<String> id = new ArrayList<>();
    public DiscordListener(Main main){
        this.main = main;
        discordVerifyCommand = new DiscordVerifyCommand(main);

    }


    public void main()
    {

        try {
            jda = JDABuilder.createDefault(
                    main.getConfig().getString("Discord-Token"))
                    .setActivity(Activity.playing("TheRift Network"))
                    .addEventListeners(this)
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
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
         id = discordVerifyCommand.VerifyCommand(event);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event){
        discordVerifyCommand.ButtonVerify(event, id);
    }
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event){main.getWelcomeMessage().onJoin(event);}

}
