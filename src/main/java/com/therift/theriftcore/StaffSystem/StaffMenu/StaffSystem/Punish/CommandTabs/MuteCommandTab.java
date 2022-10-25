package com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem.Punish.CommandTabs;

import com.therift.theriftcore.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MuteCommandTab implements TabCompleter {
    private Main main;
    public MuteCommandTab(Main main){
        this.main = main;
    }
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1){
            List<String> names;
            names = main.getPlayerManager().getAllPlayersList();
            return StringUtil.copyPartialMatches(args[0], names, new ArrayList<>());
        }

        return new ArrayList<>();
    }
}
