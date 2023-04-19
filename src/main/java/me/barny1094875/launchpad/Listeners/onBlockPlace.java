package me.barny1094875.launchpad.Listeners;

import me.barny1094875.launchpad.LaunchPad;
import me.barny1094875.launchpad.config.Config;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Vector;

public class onBlockPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        Block block = event.getBlockPlaced();

        Vector<Component> loreList = new Vector<>();
        loreList.add(Component.text("Launches you in the"));
        loreList.add(Component.text("direction you are facing"));

        // if the item placed was a launch pad
        if(item.lore() != null) {
            if (item.lore().equals(loreList)) {
                Config padConfig = LaunchPad.config();
                FileConfiguration padCoords = padConfig.getConfig();
                int padNumber = padCoords.getInt("numberOfPads") + 1;

                padCoords.set(padNumber + ".world", block.getWorld().getName());
                padCoords.set(padNumber + ".x", block.getX());
                padCoords.set(padNumber + ".y", block.getY());
                padCoords.set(padNumber + ".z", block.getZ());
                padCoords.set(padNumber + ".xpower", padCoords.getInt("defaultXpower"));
                padCoords.set(padNumber + ".ypower", padCoords.getInt("defaultYpower"));
                padCoords.set(padNumber + ".zpower", padCoords.getInt("defaultZpower"));
                // this makes it easier to search the config file
                // use CTRL+F and search for the coords of the pad
                padCoords.set(padNumber + ".searchID", block.getX() + " " + block.getY() + " " + block.getZ());

                padCoords.set("numberOfPads", padNumber);

                padConfig.save();

            }
        }
    }

}
