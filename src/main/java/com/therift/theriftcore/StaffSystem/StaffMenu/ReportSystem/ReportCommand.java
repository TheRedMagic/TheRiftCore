package com.therift.theriftcore.StaffSystem.StaffMenu.ReportSystem;

import com.therift.theriftcore.TheRiftCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReportCommand implements CommandExecutor {
    private TheRiftCore main;
    protected static ReportManager reportManager;
    public ReportCommand(TheRiftCore main){
        this.main = main;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            reportManager = new ReportManager(main);
            reportManager.openChooseMenu(((Player) sender).getPlayer());
        }
        return false;
    }
}
