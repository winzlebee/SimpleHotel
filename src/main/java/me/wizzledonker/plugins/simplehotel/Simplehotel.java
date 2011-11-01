package me.wizzledonker.plugins.simplehotel;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Simplehotel extends JavaPlugin {
    BlockListener simpleHotelListener = new SimpleHotelListener(this);
    private int[] tempCoords = new int[3];
    
    public void onDisable() {
        // TODO: Place any custom disable code here.
        saveConfig();
        System.out.println("[" + this + "]" + " has shut down...");
    }

    public void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
        
        pm.registerEvent(Type.SIGN_CHANGE, simpleHotelListener, Priority.Normal, this);
        pm.registerEvent(Type.BLOCK_BREAK, simpleHotelListener, Priority.Normal, this);
        
        System.out.println("[" + this + "]" + " by wizzledonker loaded on server.");
    }
    
    
    public void NewHotel(Player placer, String name) {
        Location loc = placer.getLocation();
        tempCoords[0] = loc.getBlockX();
        tempCoords[1] = loc.getBlockY();
        tempCoords[2] = loc.getBlockZ();
        getConfig().set("hotels." + name + ".X", tempCoords[0]);
        getConfig().set("hotels." + name + ".Y", tempCoords[1]);
        getConfig().set("hotels." + name + ".Z", tempCoords[2]);
        saveConfig();
        placer.sendMessage(ChatColor.GREEN + "You created a new hotel named " + name + "!");
    }
    public void RemoveHotel(Player player, String hotel) {
        
    }
}
