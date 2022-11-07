package com.therift.theriftcore.Discord.DiscordUntils;

import com.therift.theriftcore.TheRiftCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bukkit.Bukkit;

import java.awt.*;

public class DiscordVerify {
    private TheRiftCore main;
    public DiscordVerify(TheRiftCore main){
        this.main = main;
    }

    public void Verify(Guild guild){
            TextChannel textChannel = guild.getTextChannelById(main.getConfig().getString("DCVerify-Channel-ID"));
            if (textChannel.getLatestMessageId() == null) {
                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Verify").setDescription("By clicking the Verify button\nYou have accepted #\uD83D\uDCDC-server-rules");
                Button button = Button.success("verify", "Verify ✅");
                textChannel.sendMessageEmbeds(embedBuilder.build()).setActionRows(ActionRow.of(button)).queue();
            }
    }

    public void ButtonVerify(ButtonInteractionEvent event){
        if (Bukkit.getWorld("Spawn") != null) {
            if (event.getComponentId().equals("verify")) {
                Guild guild = event.getGuild();
                guild.addRoleToMember(event.getMember(), event.getJDA().getRoleById("1002218668983320676")).complete();
                guild.addRoleToMember(event.getMember(), event.getJDA().getRoleById("1009753702485209128")).complete();
                event.deferReply(true).queue();
                EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Account Verified");
                event.getInteraction().getHook().sendMessageEmbeds(embedBuilder.build()).queue();
            }
        }
    }
}
