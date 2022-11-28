package com.therift.theriftcore.Discord.DiscordStaff;

import com.therift.theriftcore.TheRiftCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.Bukkit;

import java.awt.*;
import java.time.Duration;
import java.util.HashMap;

public class AntiSpam {
    private TheRiftCore main;
    public AntiSpam(TheRiftCore theRiftCore){
        this.main = theRiftCore;
    }

    private HashMap<String, Integer> sendAmount = new HashMap<String, Integer>();

    public void onSend(MessageReceivedEvent e){
        if (sendAmount.containsKey(e.getMember().getId())){
            sendAmount.put(e.getMember().getId(), sendAmount.get(e.getMember().getId())+1);
        }else {
            sendAmount.put(e.getMember().getId(), 1);
        }

        if (sendAmount.get(e.getMember().getId()) == 5){
            System.out.println(sendAmount.get(e.getMember().getId()));

            EmbedBuilder a = new EmbedBuilder().setAuthor("TheRift").setColor(Color.RED)
                            .setTitle("Please do not spam");

            e.getMessage().replyEmbeds(a.build()).queue();;

            e.getMessage().delete().complete();

            if (!e.getMember().isOwner()) {
                e.getMember().timeoutFor(Duration.ofSeconds(3));
            }

            if (main.amountSend.getAmount().containsKey(e.getMember().getId())){
                main.amountSend.getAmount().put(e.getMember().getId(), main.amountSend.getAmount().get(e.getMember().getId())-5);
            }
        }

        Bukkit.getScheduler().runTaskLater(main, () ->{
            if (sendAmount.containsKey(e.getMember().getId())){
                sendAmount.put(e.getMember().getId(), sendAmount.get(e.getMember().getId())-1);
            }
        }, 80);
    }
}
