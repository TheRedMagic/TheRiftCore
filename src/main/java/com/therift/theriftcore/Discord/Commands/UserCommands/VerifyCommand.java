package com.therift.theriftcore.Discord.Commands.UserCommands;

import com.therift.theriftcore.TheRiftCore;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Random;

public class VerifyCommand implements CommandExecutor {
    private TheRiftCore main;

    public VerifyCommand(TheRiftCore main){
        this.main = main;
    }


    public static HashMap<String, Integer> VerifyCode = new HashMap<>();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            if (((Player) sender).getWorld().getName().equals("Spawn")) {

                Player player = (Player) sender;
                if (VerifyCode.containsValue(player.getDisplayName())) {
                    VerifyCode.remove(player.getDisplayName());
                    Random rnd = new Random();
                    int number = rnd.nextInt(10000);


                    CodeMessage(number, player);
                    VerifyCode.put(player.getDisplayName(), number);
                } else {
                    Random rnd = new Random();
                    int number = rnd.nextInt(10000);

                    CodeMessage(number, player);
                    VerifyCode.put(player.getDisplayName(), number);


                }
            }else {
                System.out.println(ChatColor.RED + "Must be is spawn");
            }
        }
        return false;
    }

    private void CodeMessage(Integer numder, Player player){

        TextComponent code = new TextComponent("§3" + numder.toString());
        code.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to copy the code.")));
        code.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, numder.toString()));

        TextComponent Start = new TextComponent("§7§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n\n" +
                "§r§6Use this code in #bot-commands by using /verify.\nCode : ");
        TextComponent End = new TextComponent("\n\n§7§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        Start.addExtra(code);
        Start.addExtra(End);

        player.spigot().sendMessage(Start);
    }
}



