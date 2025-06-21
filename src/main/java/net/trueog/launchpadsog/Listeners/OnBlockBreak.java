package net.trueog.launchpadsog.Listeners;

import java.util.Collections;
import java.util.Vector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.trueog.launchpadsog.LaunchPadsOG;
import net.trueog.launchpadsog.config.Config;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OnBlockBreak implements Listener {

    // check to see if the block broken was a launch pad
    // if so, remove the launch pad from the config list
    // and update all id numbers after that one
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Config padConfig = LaunchPadsOG.config();
        FileConfiguration padCoords = padConfig.getConfig();
        Block block = event.getBlock();
        World world = block.getWorld();
        Player player = event.getPlayer();
        int numberOfPads = padCoords.getInt("numberOfPads");

        // check if the launch pad is craftable. If so, then players can break launchpads
        // to get them back/remove them
        // also check if the player has the permission launchpad.canbreak
        // if either of these are true, then allow the pad to be broken
        //        if(padCoords.getBoolean("cancraft") || player.hasPermission("launchpad.canbreak")) {

        for (int i = 1; i < numberOfPads + 1; i++) {
            if (padCoords.getString("" + i + ".world").equals(world.getName())) {
                if (padCoords.getInt("" + i + ".x") == block.getX()) {
                    if (padCoords.getInt("" + i + ".z") == block.getZ()) {
                        if (padCoords.getInt("" + i + ".y") == block.getY()) {

                            // check if the launch pad is craftable. If so, then players can break launchpads
                            // to get them back/remove them
                            // also check if the player has the permission launchpad.canbreak
                            // if either of these are true, then allow the pad to be broken
                            if (padCoords.getBoolean("cancraft") || player.hasPermission("launchpad.canbreak")) {
                                // the block has the coordinates of this pad
                                // remove the pad from the list and move
                                // all other pads up one id number
                                for (int j = i; j < padCoords.getInt("numberOfPads") + 1; j++) {
                                    padCoords.set("" + j + ".x", padCoords.getInt("" + (j + 1) + ".x"));
                                    padCoords.set("" + j + ".y", padCoords.getInt("" + (j + 1) + ".y"));
                                    padCoords.set("" + j + ".z", padCoords.getInt("" + (j + 1) + ".z"));
                                    padCoords.set("" + j + ".world", padCoords.getString("" + (j + 1) + ".world"));
                                    padCoords.set("" + j + ".xpower", padCoords.getDouble("" + (j + 1) + ".xpower"));
                                    padCoords.set("" + j + ".ypower", padCoords.getDouble("" + (j + 1) + ".ypower"));
                                    padCoords.set("" + j + ".zpower", padCoords.getDouble("" + (j + 1) + ".zpower"));
                                    // this makes it easier to search the config file
                                    // use CTRL+F and search for the coords of the pad
                                    padCoords.set(
                                            "" + j + ".searchID", padCoords.getString("" + (j + 1) + ".searchID"));
                                }
                                padCoords.set("" + numberOfPads, null);
                                padCoords.set("numberOfPads", numberOfPads - 1);
                                padConfig.save();

                                // if the block there was a pressure plate,
                                // then a launch pad item was placed
                                // and therefore should be dropped
                                if (event.getBlock().getType() == Material.STONE_PRESSURE_PLATE) {
                                    // create the launchpad item
                                    Vector<Component> loreList = new java.util.Vector<>();

                                    ItemStack launchPad = new ItemStack(Material.STONE_PRESSURE_PLATE);
                                    ItemMeta padMeta = launchPad.getItemMeta();

                                    padMeta.displayName(
                                            Component.text("Launch Pad").decorate(TextDecoration.ITALIC));

                                    loreList.add(Component.text("Launches you in the"));
                                    loreList.add(Component.text("direction you are facing"));
                                    padMeta.lore(Collections.list(loreList.elements()));
                                    // add a useless enchantment to the item to make it
                                    // stand out in the inventory
                                    padMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
                                    launchPad.setItemMeta(padMeta);

                                    // make sure that the pressure plate is not dropped
                                    event.setDropItems(false);
                                    // drop the launch pad item
                                    world.dropItem(block.getLocation(), launchPad);
                                }

                                break;
                            }
                            // if the player is not allowed to break the launchpad, cancel the event
                            else {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
        //        }
    }
}
