package me.wizzledonker.plugins.simplehotel;

import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;

public class Simplehotel extends JavaPlugin {
    BlockListener simpleHotelListener = new SimpleHotelListener(this);
    PlayerListener simpleHotelPlayerListener = new SimpleHotelPlayerListener(this);
    CommandExecutor hotelCommands = new SimpleHotelCommands(this);
    public boolean isBlockCancelled = false;
    public static Economy economy = null;
    
    public void onDisable() {
        // TODO: Place any custom disable code here.
        System.out.println("[" + this + "]" + " has shut down...");
    }

    public void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
        
        pm.registerEvent(Type.SIGN_CHANGE, simpleHotelListener, Priority.Normal, this);
        pm.registerEvent(Type.BLOCK_BREAK, simpleHotelListener, Priority.Normal, this);
        pm.registerEvent(Type.PLAYER_INTERACT, simpleHotelPlayerListener, Priority.Normal, this);
        
        //Stuff about registering Vault to work with the server + commands
        if (setupEconomy()) {
            System.out.println("[" + this + "]" + " successfully hooked with " + economy.getName());
        } else {
            System.out.println("[" + this + "]" + " error enabling economy via Vault! Disabling plugin...");
            pm.disablePlugin(this);
        }
        
        getCommand("hotel").setExecutor(hotelCommands);
        
        getCommand("delhotel").setExecutor(hotelCommands);
        
        getCommand("listhotels").setExecutor(hotelCommands);
        
        System.out.println("[" + this + "]" + " by wizzledonker loaded on server.");
    }
    
    private Boolean setupEconomy() {
        //Sets up Vault to be used with the plugin
        Plugin vault = this.getServer().getPluginManager().getPlugin("Vault");
        if (vault == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
    
    public void NewHotel(Player placer, String name, Double price) {
        Location loc = placer.getLocation();
        getConfig().set("hotels." + name + ".X", loc.getBlockX());
        getConfig().set("hotels." + name + ".Y", loc.getBlockY());
        getConfig().set("hotels." + name + ".Z", loc.getBlockZ());
        getConfig().set("hotels." + name + ".price", price);
        saveConfig();
        placer.sendMessage(ChatColor.GREEN + "You created a new hotel named " + ChatColor.WHITE + name + "!");
    }
    
    public void GotoHotel(Player player, String hotel) {
        if (getConfig().contains("hotels." + hotel)) {
            Double price = getConfig().getDouble("hotels." + hotel + ".price");
            
            Location loc = new Location( player.getWorld(), 
                getConfig().getInt("hotels." + hotel + ".X"),
                getConfig().getInt("hotels." + hotel + ".Y"),
                getConfig().getInt("hotels." + hotel + ".Z") );
            
            economy.withdrawPlayer(player.getName(), price);
            player.teleport(loc);
            player.sendMessage(ChatColor.GREEN + "Successfully checked in to hotel " + ChatColor.WHITE + hotel);
        } else {
            player.sendMessage(ChatColor.RED + "Not a valid Hotel! Does it exist?");
        }
        
    }
    public void SignRemoveHotel(Player player, String hotel) {
        if (player.hasPermission("SimpleHotel.remove")) {
            if (getConfig().contains("hotels." + hotel)) {
                getConfig().set("hotels." + hotel, null);
                player.sendMessage(ChatColor.DARK_GREEN + "Successfully removed hotel " + ChatColor.WHITE + hotel);
            } else {
                player.sendMessage(ChatColor.RED + "Hotel does not exist! (deleted by other means?)");
            }
            isBlockCancelled = false;
        } else {
            player.sendMessage(ChatColor.RED + "You do not have permission to remove this hotel!");
            isBlockCancelled = true;
        }
    }
    public String getHotels() {
        Set<String> keys = getConfig().getConfigurationSection("hotels").getKeys(false);
        return keys.toString();
    };
}
