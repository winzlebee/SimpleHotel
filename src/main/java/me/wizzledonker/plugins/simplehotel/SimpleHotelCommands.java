/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.wizzledonker.plugins.simplehotel;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Win
 */
public class SimpleHotelCommands implements CommandExecutor{
    public static Simplehotel plugin;

    SimpleHotelCommands(Simplehotel instance) {
        plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        
        if (alias.equalsIgnoreCase("hotel")) {
           //First, convert the arguments to a string for the function
            String argsStr = null;
            if (cs instanceof Player) {
                //if the player is actually a player, not a console
                Player sender = (Player) cs;
                if (sender.hasPermission("SimpleHotel.canbuy")) {
                    if (args.length == 1) {
                        argsStr = args[0];
                        plugin.GotoHotel(sender, argsStr);
                        return true;
                    } else {
                        cs.sendMessage("Correct Usage:");
                        //underneath this message, the 'usage' text from plugin.yml is printed
                        return false;
                    }  
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have permission!");
                }

            } else {
                //else, tell the console that he may not use the command
                cs.sendMessage("Only players may use that command!");
                return false;
            }


            return true; 
        }
        
        //Now for deleting hotels!
        if (alias.equalsIgnoreCase("delhotel")) {
            //The method for '/delhotel <hotelroom>'
            if (cs instanceof Player) {
                if (args.length == 1) {
                    String argsStr = args[0];
                    plugin.SignRemoveHotel((Player) cs, argsStr);
                } else {
                    cs.sendMessage("Correct Usage:");
                    //same principal as above command
                    return false;
                }
            } else {
                //else, tell the console that he may not use the command
                cs.sendMessage("Only players may use that command!");
                return false;
            }
            return true;
        }
        return false;
   }
}
