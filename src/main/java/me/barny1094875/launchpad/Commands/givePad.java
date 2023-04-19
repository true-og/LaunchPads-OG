package me.barny1094875.launchpad.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Vector;

public class givePad implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(command.getName().equalsIgnoreCase("givepad")) {
            // if it's just /givepad
            if (args.length == 0) {
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                } else {
                    return false;
                }

                if (player.hasPermission("launchpad.cangive")) {

                    // create the launchpad item
                    Vector<Component> loreList = new java.util.Vector<>();

                    ItemStack launchPad = new ItemStack(Material.STONE_PRESSURE_PLATE);
                    ItemMeta padMeta = launchPad.getItemMeta();

                    padMeta.displayName(Component.text("Launch Pad")
                            .decorate(TextDecoration.ITALIC));

                    loreList.add(Component.text("Launches you in the"));
                    loreList.add(Component.text("direction you are facing"));
                    padMeta.lore(Collections.list(loreList.elements()));
                    // add a useless enchantment to the item to make it
                    // stand out in the inventory
                    padMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
                    launchPad.setItemMeta(padMeta);

                    // give the launchpad item to the player
                    player.getInventory().addItem(launchPad);

                }
            }
        }

        return true;
    }
}
