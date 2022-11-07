package com.therift.theriftcore.Database.DataBaseConnection;

import com.therift.theriftcore.TheRiftCore;
import org.bukkit.configuration.Configuration;

public class DatabaseConfig {
    private TheRiftCore main;
    private Configuration configuration;
    private String Host;
    private String Username;
    private String Password;
    private String Port;


    public DatabaseConfig(TheRiftCore main) {
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
