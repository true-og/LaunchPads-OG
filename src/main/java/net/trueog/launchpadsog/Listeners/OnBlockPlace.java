package net.trueog.launchpadsog.Listeners;

import java.util.Vector;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.trueog.launchpadsog.LaunchPadsOG;

public class OnBlockPlace implements Listener {

    // ignoreCancelled: a placement a region plugin blocked must not become a
    // working pad on disk anyway
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {

        ItemStack item = event.getItemInHand();
        Block block = event.getBlockPlaced();

        // the pad item is a pressure plate; lore alone must not qualify
        if (item.getType() != Material.STONE_PRESSURE_PLATE) {

            return;

        }

        Vector<Component> loreList = new Vector<>();
        loreList.add(Component.text("Launches you in the"));
        loreList.add(Component.text("direction you are facing"));

        // if the item placed was a launch pad
        if (item.lore() != null) {

            if (item.lore().equals(loreList)) {

                FileConfiguration padCoords = LaunchPadsOG.getConfiguration();
                int padNumber = padCoords.getInt("numberOfPads") + 1;

                padCoords.set(padNumber + ".world", block.getWorld().getName());
                padCoords.set(padNumber + ".x", block.getX());
                padCoords.set(padNumber + ".y", block.getY());
                padCoords.set(padNumber + ".z", block.getZ());
                padCoords.set(padNumber + ".xpower", padCoords.getDouble("defaultXpower"));
                padCoords.set(padNumber + ".ypower", padCoords.getDouble("defaultYpower"));
                padCoords.set(padNumber + ".zpower", padCoords.getDouble("defaultZpower"));
                // this makes it easier to search the config file
                // use CTRL+F and search for the coords of the pad
                padCoords.set(padNumber + ".searchID", block.getX() + " " + block.getY() + " " + block.getZ());
                padCoords.set("numberOfPads", padNumber);
                LaunchPadsOG.getPlugin().saveConfig();

            }

        }

    }

}
