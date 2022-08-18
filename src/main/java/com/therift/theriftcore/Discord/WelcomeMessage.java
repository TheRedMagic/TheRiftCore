package com.therift.theriftcore.Discord;


import com.therift.theriftcore.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.awt.*;

public class WelcomeMessage {
    private Main main;
    public WelcomeMessage(Main main){
        this.main = main;
    }

    public void onJoin(GuildMemberJoinEvent event){

     EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green).setAuthor("TheRift").setTitle("Welcome " + event.getMember().getAsMention() + " to TheRift").setDescription("This is the discord server for: \nTheRift Minecraft server\nMore info in #server-rules");
        event.getGuild().getTextChannelById(main.getConfig().getString("welcome-Channel")).sendMessageEmbeds(embedBuilder.build());
    }
}
