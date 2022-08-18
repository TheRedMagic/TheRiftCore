package com.therift.theriftcore;

import com.therift.theriftcore.Core.CoreListener;
import com.therift.theriftcore.Database.DatabaseConfig;
import com.therift.theriftcore.Database.PlayerManager;
import com.therift.theriftcore.Database.ResetDataCommand;
import com.therift.theriftcore.Database.database;
import com.therift.theriftcore.Discord.DiscordListener;
import com.therift.theriftcore.Discord.VerifyCommand;
import com.therift.theriftcore.MainCommands.SpawnCommand;
import com.therift.theriftcore.StaffSystem.AllPlayersCommand;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import javax.security.auth.login.LoginException;

public final class Main extends JavaPlugin {

    //Calls
    private DatabaseConfig databaseConfig;
    private database database;
    private AllPlayersCommand allPlayersCommand;
    private PlayerManager playerManager;
    private CoreListener coreListener;
    private DiscordListener discordListener;
    private VerifyCommand verifyCommand;



    // Var
    private Server server;


    @Override
    public void onEnable() {
        //Databases
        databaseConfig = new DatabaseConfig(this);
        database = new database(this);
        allPlayersCommand = new AllPlayersCommand(this);
        playerManager = new PlayerManager(this);
        database.connect();
        server = getServer();


        //Core
        coreListener = new CoreListener(this);

        //Commands
        getCommand("AllPlayers").setExecutor(new AllPlayersCommand(this));
        getCommand("Spawn").setExecutor(new SpawnCommand(this));
        getCommand("ResetData").setExecutor(new ResetDataCommand(this));
        getCommand("Verify").setExecutor(new VerifyCommand(this));


        //Listener
        Bukkit.getPluginManager().registerEvents(new CoreListener(this), this);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", (PluginMessageListener) new CoreListener(this));
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        //Discord
        discordListener = new DiscordListener(this);
        verifyCommand = new VerifyCommand(this);
        discordListener.main();

    }

    @Override
    public void onDisable() {
        database.disconnect();

        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);

        VerifyCommand.VerifyCode.clear();

    }

    public DatabaseConfig getDatabaseConfig(){return databaseConfig;}
    public database getDatabase(){return database;}
    public PlayerManager getPlayerManager() {return playerManager;}
}
