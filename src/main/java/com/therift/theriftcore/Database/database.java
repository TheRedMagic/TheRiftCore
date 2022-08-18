package com.therift.theriftcore.Database;

import com.therift.theriftcore.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class database {

    private Main main;
    private Connection connection;
    public database(Main main){
        this.main = main;
    }

    public void connect(){
        this.main = main;


        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + main.getDatabaseConfig().getHost() + ":" + main.getDatabaseConfig().getPort() + "/" + main.getDatabaseConfig().getUsername() + "?autoReconnect=true",
                    main.getDatabaseConfig().getUsername(),
                    main.getDatabaseConfig().getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.print("Connect to database");
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
