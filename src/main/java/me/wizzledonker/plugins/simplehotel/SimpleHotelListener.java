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
            if (!player.hasPermission("SimpleHotel.create")) {
                player.sendMessage(ChatColor.RED + "You are not allowed to create a hotel!");
                return;
            }
            if (event.getLine(1).isEmpty()) {
                player.sendMessage(ChatColor.DARK_GREEN + "Please fill in the missing values!");
                return;
            }
            //Changing the colours to fancy!
            event.setLine(0, ChatColor.GREEN + event.getLine(0));
            event.setLine(1, event.getLine(1));
            //Run the final function to create the hotel
            plugin.NewHotel(player, event.getLine(1).toString());
        }
    }
    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.SIGN) {
            Sign broken = (Sign) event.getBlock();
            Player player = event.getPlayer();
            if (player.hasPermission("SimpleHotel.remove")) {
                String brokenName = broken.getLine(1).toString();
                plugin.RemoveHotel(player, brokenName);
            }
        }
    }
}
