package com.therift.theriftcore.Core;

import net.luckperms.api.model.user.User;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Location;

import java.util.Map;
import java.util.UUID;

public class NPC {

    public static Map<Integer, NPC> npcs;
    private String Username;
    private UUID uuid;
    private String UserNameSkin;
    private UUID uuidSkin;
    private String tabName;
    private Location location;
    private boolean inTab;
    public NPC(String Username, Location location){
        this.Username = Username;
        this.location = location;
    }
    public NPC(UUID uuid, Location location){
        this.uuid = uuid;
        this.location = location;
    }
    public NPC(String Username, UUID uuid, Location location){
        this.Username = Username;
        this.uuid = uuid;
        this.location = location;
    }
    public NPC(String UserName, String tabName, Location location){
        this.Username = UserName;
        this.tabName = tabName;
        this.location = location;
    }
    public NPC(String Username, String UserNameSkin, String tabName, boolean inTab, Location location){
        this.Username = Username;
        this.UserNameSkin = UserNameSkin;
        this.tabName = tabName;
        this.location = location;
        this.inTab = inTab;
    }
    public NPC(UUID uuid, String UserNameSkin, String tabName, boolean inTab, Location location){
        this.uuid = uuid;
        this.UserNameSkin = UserNameSkin;
        this.tabName = tabName;
        this.inTab = inTab;
        this.location = location;
    }
    public NPC(String Username, UUID uuidSkin, String tabName, boolean inTab, Location location){
        this.Username = Username;
        this.uuidSkin = uuidSkin;
        this.tabName = tabName;
        this.location = location;
        this.inTab = inTab;
    }
    public NPC(UUID uuid, UUID uuidSkin, String tabName, boolean inTab, Location location){
        this.uuid = uuid;
        this.uuidSkin = uuidSkin;
        this.tabName = tabName;
        this.inTab = inTab;
        this.location = location;
    }

    public void Spawn(Boolean everyone, UUID uuid){


    }

}
