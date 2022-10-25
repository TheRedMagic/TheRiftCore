package com.therift.theriftcore.Database.DataBaseConnection;

import com.google.common.io.ByteStreams;
import com.therift.theriftcore.Main;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class DatabaseConfig {
    private Main main;
    private Configuration configuration;
    private String Host;
    private String Username;
    private String Password;
    private String Port;


    public DatabaseConfig(Main main) {
        this.main = main;

        main.getConfig().options().copyDefaults();
        main.saveDefaultConfig();

        Host = main.getConfig().getString("Host");
        Username = main.getConfig().getString("Username");
        Password = main.getConfig().getString("Password");
        Port = main.getConfig().getString("Port");
    }

    public String getHost(){return Host;}
    public String getUsername(){return Username;}
    public String getPassword(){return Password;}
    public String getPort(){return Port;}


}
