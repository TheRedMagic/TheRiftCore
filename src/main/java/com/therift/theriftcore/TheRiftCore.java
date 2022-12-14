package com.therift.theriftcore;

import com.therift.theriftcore.Core.CoreListener;
import com.therift.theriftcore.Core.GlobalChat;
import com.therift.theriftcore.Core.LuckPermListener;
import com.therift.theriftcore.Database.DataBaseConnection.DatabaseConfig;
import com.therift.theriftcore.Database.DataBaseConnection.database;
import com.therift.theriftcore.Database.DatabaseCommands.ResetDataCommand;
import com.therift.theriftcore.Database.DatabaseCommands.ResetDataTab;
import com.therift.theriftcore.Database.DatabaseManager.PlayerManager;
import com.therift.theriftcore.Database.DatabaseManager.RiftPlayer;
import com.therift.theriftcore.Discord.*;
import com.therift.theriftcore.Discord.Commands.UserCommands.HighScoreMessager;
import com.therift.theriftcore.Discord.Commands.UserCommands.UserStats;
import com.therift.theriftcore.Discord.Commands.UserCommands.VerifyCommand;
import com.therift.theriftcore.Discord.DiscordUntils.AmountSend;
import com.therift.theriftcore.Discord.DiscordUntils.DiscordVerify;
import com.therift.theriftcore.Discord.DiscordUntils.Roles;
import com.therift.theriftcore.Discord.Games.DiscordCounter;
import com.therift.theriftcore.MainCommands.SpawnCommand;
import com.therift.theriftcore.StaffSystem.AllPlayersCommand;
import com.therift.theriftcore.StaffSystem.StaffMenu.ReportSystem.ReportCommand;
import com.therift.theriftcore.StaffSystem.StaffMenu.ReportSystem.ReportListener;
import com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem.Punish.CommandTabs.*;
import com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem.Punish.Commands.*;
import com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem.StaffMode.StaffCommand;
import com.therift.theriftcore.StaffSystem.StaffMenu.StaffSystem.StaffMode.StaffListener;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;


public final class TheRiftCore extends JavaPlugin {

    //Calls
    private DatabaseConfig databaseConfig;
    private com.therift.theriftcore.Database.DataBaseConnection.database database;
    private AllPlayersCommand allPlayersCommand;
    private PlayerManager playerManager;
    private DiscordListener discordListener;
    private VerifyCommand verifyCommand;
    private DiscordVerify discordVerify;
    private LuckPerms api;
    private Roles roles;
    public static TheRiftCore main;
    private DiscordCounter discordCounter;
    public AmountSend amountSend;
    public UserStats userStats;
    public HighScoreMessager highScoreMessager;
    @Override
    public void onEnable() {

        //Databases
        databaseConfig = new DatabaseConfig(this);
        database = new database(this);
        allPlayersCommand = new AllPlayersCommand(this);
        playerManager = new PlayerManager(this);
        database.connect();
        main = this;

        //Commands
        getCommand("AllPlayers").setExecutor(new AllPlayersCommand(this));
        getCommand("Spawn").setExecutor(new SpawnCommand(this));
        getCommand("ResetData").setExecutor(new ResetDataCommand(this));
        getCommand("ResetData").setTabCompleter(new ResetDataTab(this));
        getCommand("Verify").setExecutor(new VerifyCommand(this));
        getCommand("Staff").setExecutor(new StaffCommand(this));
        getCommand("Unban").setExecutor(new unbanCommand(this));
        getCommand("Unban").setTabCompleter(new UnBanTab(this));
        getCommand("Pardon").setExecutor(new pardonCommand(this));
        getCommand("Pardon").setTabCompleter(new UnBanTab(this));
        getCommand("Report").setExecutor(new ReportCommand(this));
        getCommand("Ban").setExecutor(new BanCommand(this));
        getCommand("Ban").setTabCompleter(new BanCommandTab(this));
        getCommand("Warn").setExecutor(new WarmCommand(this));
        getCommand("Warn").setTabCompleter(new WarnCommandTab(this));
        getCommand("Kick").setExecutor(new KickCommand(this));
        getCommand("Kick").setTabCompleter(new KickCommandTab(this));
        getCommand("Mute").setExecutor(new MuteCommand(this));
        getCommand("Mute").setTabCompleter(new MuteCommandTab(this));
        getCommand("ChatMute").setExecutor(new ChatMuteCommand());


        //Listener
        Bukkit.getPluginManager().registerEvents(new CoreListener(this), this);
        Bukkit.getPluginManager().registerEvents(new StaffListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ReportListener(), this);
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new GlobalChat(this));
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new ResetDataCommand(this));
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        //Discord
        userStats = new UserStats(this);
        highScoreMessager = new HighScoreMessager(this);
        discordListener = new DiscordListener(this);
        verifyCommand = new VerifyCommand(this);
        discordListener.main();
        discordVerify = new DiscordVerify(this);
        discordCounter = new DiscordCounter(this);
        amountSend = new AmountSend(this);




        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
            new LuckPermListener(this, api);
        }

    }

    @Override
    public void onDisable() {

        if (discordCounter != null){
            discordCounter.onDis();
        }

        amountSend.saveAmount();


        database.disconnect();

        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);

        VerifyCommand.VerifyCode.clear();



    }

    public DatabaseConfig getDatabaseConfig(){return databaseConfig;}
    public DiscordCounter getDiscordCounter(){return discordCounter;}
    public database getDatabase(){return database;}
    public PlayerManager getPlayerManager() {return playerManager;}
    public LuckPerms getApi() { return api;}
    public RiftPlayer getRiftPlayer(UUID uuid){
        return new RiftPlayer(uuid);
    }
}
