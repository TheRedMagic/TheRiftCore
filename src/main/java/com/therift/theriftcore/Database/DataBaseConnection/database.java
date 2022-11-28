package com.therift.theriftcore.Database.DataBaseConnection;

import com.therift.theriftcore.TheRiftCore;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class database {

    private TheRiftCore main;
    private Connection connection;
    public database(TheRiftCore main){
        this.main = main;
    }

    public void connect(){

        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + main.getDatabaseConfig().getHost() + ":" + main.getDatabaseConfig().getPort() + "/" + main.getDatabaseConfig().getUsername() + "?autoReconnect=true",
                        main.getDatabaseConfig().getUsername(),
                        main.getDatabaseConfig().getPassword());
                connection.prepareStatement("SET SESSION idle_transaction_timeout='0';");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.print("Connect to database");
        }, 0, 432000);

    }

    public boolean isConnected(){return connection != null;}
    public Connection getConnection(){return connection;}

    public void disconnect(){
        if (isConnected()){
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
