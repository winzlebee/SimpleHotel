/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.wizzledonker.plugins.simplehotel;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author Win
 */
public class SimpleHotelListener extends BlockListener{
    public static Simplehotel plugin;
    
    public SimpleHotelListener(Simplehotel instance) {
        plugin = instance;
    }
    
    @Override
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (event.getLine(0).toLowerCase().contains("[hotel]")) {
            if (!player.hasPermission("SimpleHotel.create.hotel")) {
                player.sendMessage(ChatColor.RED + "You are not allowed to create a hotel!");
                event.setLine(0, "The hotel:");
                return;
            }
            if (event.getLine(1).isEmpty() || event.getLine(2).isEmpty()) {
                player.sendMessage(ChatColor.DARK_GREEN + "Please fill in the missing values!");
                return;
            }
            //Changing the colours to fancy!
            event.setLine(0, ChatColor.GREEN + event.getLine(0));
            //Run the final function to create the hotel
            plugin.NewHotel(player, event.getLine(1).toString(), Double.parseDouble(event.getLine(2).toString()));
        }
        if (event.getLine(0).toLowerCase().contains("[check-in]")) {
            if (!player.hasPermission("SimpleHotel.create.checkin")) {
                player.sendMessage(ChatColor.RED + "You are not allowed to create a hotel check-in!");
                event.setLine(0, "A hotel:");
                return;
            }
            if (event.getLine(1).isEmpty()) {
                player.sendMessage(ChatColor.DARK_GREEN + "Please fill in the missing values!");
                return;
            }
            //Changing the colours to fancy!
            event.setLine(0, ChatColor.GOLD + event.getLine(0));
            //Sends the player a message saying the creation was successful
            player.sendMessage(ChatColor.GREEN + "Created a check-in sign to the hotel " + ChatColor.WHITE + event.getLine(1));
        }
    }
    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType().equals(Material.WALL_SIGN ) || event.getBlock().getType().equals(Material.SIGN_POST)) {
            Sign broken = (Sign) event.getBlock().getState();
            Player player = event.getPlayer();
            if (broken.getLine(0).toLowerCase().contains("[hotel]")) {
                plugin.SignRemoveHotel(player, broken.getLine(1));
                event.setCancelled(plugin.isBlockCancelled);
            }
        }
    }
}
