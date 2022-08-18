package com.therift.theriftcore.Discord;

import com.therift.theriftcore.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class DiscordVerify {
    private Main main;
    public DiscordVerify(Main main){
        this.main = main;
    }

    public void Verify(ReadyEvent event){

        Guild guild = event.getJDA().getGuildById(main.getConfig().getString("GuildID"));
        if (guild.getTextChannelById(main.getConfig().getString("DCVerify-Channel-ID")).getHistory().isEmpty()) {
            EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Verify").setDescription("Click the button to verify");
            Channel channel = (Channel) guild.getTextChannelById(main.getConfig().getString("DCVerify-Channel-ID")).sendMessageEmbeds(embedBuilder.build()).setActionRow(Button.success("verify", "Verify"));
        }
    }

    public void ButtonVerify(ButtonInteractionEvent event){
        if (event.getComponentId().equals("verify")){
            Guild guild = event.getJDA().getGuildById(main.getConfig().getString("GuildID"));
            guild.addRoleToMember(event.getMember(), event.getJDA().getRoleById("1002218668983320676"));
        }
    }
}
