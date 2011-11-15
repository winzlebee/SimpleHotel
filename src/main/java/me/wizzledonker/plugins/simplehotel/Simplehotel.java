package me.wizzledonker.plugins.simplehotel;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;

public class Simplehotel extends JavaPlugin {
    BlockListener simpleHotelListener = new SimpleHotelListener(this);
    CommandExecutor hotelCommands = new SimpleHotelCommands(this);
    private int[] tempCoords = new int[3];
    public boolean isBlockCancelled = false;
    public static Economy economy = null;
    
    public void onDisable() {
        // TODO: Place any custom disable code here.
        saveConfig();
        System.out.println("[" + this + "]" + " has shut down...");
    }

    public void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
        
        pm.registerEvent(Type.SIGN_CHANGE, simpleHotelListener, Priority.Normal, this);
        pm.registerEvent(Type.BLOCK_BREAK, simpleHotelListener, Priority.Normal, this);
        
        //Stuff about registering Vault to work with the server + commands
        if (setupEconomy()) {
            System.out.println("[" + this + "]" + " successfully hooked with " + economy.getName());
        } else {
            System.out.println("[" + this + "]" + " error enabling economy via Vault! Disabling plugin...");
            pm.disablePlugin(this);
        }
        
        getCommand("hotel").setExecutor(hotelCommands);
        
        getCommand("delhotel").setExecutor(hotelCommands);
        
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
        tempCoords[0] = loc.getBlockX();
        tempCoords[1] = loc.getBlockY();
        tempCoords[2] = loc.getBlockZ();
        getConfig().set("hotels." + name + ".X", tempCoords[0]);
        getConfig().set("hotels." + name + ".Y", tempCoords[1]);
        getConfig().set("hotels." + name + ".Z", tempCoords[2]);
        getConfig().set("hotels." + name + ".price", price);
        saveConfig();
        placer.sendMessage(ChatColor.GREEN + "You created a new hotel named " + ChatColor.WHITE + name + "!");
    }
    
    public void GotoHotel(Player player, String hotel) {
        if (getConfig().contains("hotels." + hotel)) {
            Double price = getConfig().getDouble("hotels." + hotel + ".price");
            tempCoords[0] = getConfig().getInt("hotels." + hotel + ".X");
            tempCoords[1] = getConfig().getInt("hotels." + hotel + ".Y");
            tempCoords[2] = getConfig().getInt("hotels." + hotel + ".Z");
            economy.withdrawPlayer(player.toString(), price);
            player.teleport(new Location(player.getWorld(), tempCoords[0], tempCoords[1], tempCoords[2]));
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
}
