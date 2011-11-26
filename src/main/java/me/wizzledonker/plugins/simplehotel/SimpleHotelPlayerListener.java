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
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

/**
 *
 * @author Win
 */
public class SimpleHotelPlayerListener extends PlayerListener {
    public static Simplehotel plugin;
    
    public SimpleHotelPlayerListener(Simplehotel instance) {
        plugin = instance;
    }
    
    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            if (!(block.getType().equals(Material.WALL_SIGN) || block.getType().equals(Material.SIGN_POST))) {
                return;
            }
            if (!((Sign) block.getState()).getLine(0).toLowerCase().contains("[check-in]")) {
                return;
            }
            if (((Sign) block.getState()).getLine(1).isEmpty()) {
                player.sendMessage("One of the lines on this sign is missing!");
                return;
            }
            if (!player.hasPermission("SimpleHotel.canbuy")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to check-in to this hotel!");
                return;
            }
            Sign sign = ((Sign) block.getState());
            plugin.GotoHotel(player, sign.getLine(1));
        }
    }
    
}
