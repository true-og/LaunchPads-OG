package net.trueog.launchpadsog.Commands;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.trueog.launchpadsog.LaunchPadsOG;

public class LaunchPadCommands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args)
    {

        if (command.getName().equalsIgnoreCase("launchpad")) {

            // check if a subcommand was provided
            if (args.length > 0) {

                if (sender instanceof Player) {

                    Player player = (Player) sender;
                    FileConfiguration padCoords = LaunchPadsOG.getConfiguration();
                    int numberOfPads = padCoords.getInt("numberOfPads");
                    World world = player.getWorld();

                    // if the command is /launchpad set
                    if (args[0].equalsIgnoreCase("set")) {

                        if (sender.hasPermission("launchpad.canset")) {

                            // if the command is just /launchpad setpad
                            // set a new pad to the player's location
                            // and set the power to the default values
                            if (args.length == 1) {

                                Location playerLocation = player.getLocation();
                                int padNumber = padCoords.getInt("numberOfPads") + 1;
                                int playerX = playerLocation.getBlockX();
                                int playerY = playerLocation.getBlockY();
                                int playerZ = playerLocation.getBlockZ();

                                // check if there is already a pad in that location
                                for (int i = 1; i < numberOfPads + 1; i++) {

                                    if (padCoords.getString("" + i + ".world").equals(world.getName())) {

                                        if (padCoords.getInt("" + i + ".x") == playerX) {

                                            if (padCoords.getInt("" + i + ".z") == playerZ) {

                                                if (padCoords.getInt("" + i + ".y") == playerY) {

                                                    // there's a pad there, so let the player know
                                                    // and stop the rest of the command from happening
                                                    sender.sendMessage(Component.text("[LaunchPad]")
                                                            .color(TextColor.color(0, 255, 0))
                                                            .append(Component.text(" There's Already a Pad There!")
                                                                    .color(TextColor.color(255, 0, 0))));
                                                    return true;

                                                }

                                            }

                                        }

                                    }

                                }

                                padCoords.set(padNumber + ".world", player.getWorld().getName());
                                padCoords.set(padNumber + ".x", playerX);
                                padCoords.set(padNumber + ".y", playerY);
                                padCoords.set(padNumber + ".z", playerZ);
                                padCoords.set(padNumber + ".xpower", padCoords.getDouble("defaultXpower"));
                                padCoords.set(padNumber + ".ypower", padCoords.getDouble("defaultYpower"));
                                padCoords.set(padNumber + ".zpower", padCoords.getDouble("defaultZpower"));
                                // this makes it easier to search the config file
                                // use CTRL+F and search for the coords of the pad
                                padCoords.set(padNumber + ".searchID", playerX + " " + playerY + " " + playerZ);

                                padCoords.set("numberOfPads", padNumber);

                                LaunchPadsOG.getPlugin(LaunchPadsOG.class).saveConfig();

                                // tell the player that the pad has been set at their coordinates
                                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                        .append(Component
                                                .text(" Pad Set at: " + playerX + ", " + playerY + ", " + playerZ)
                                                .color(TextColor.color(255, 255, 0))));

                            }
                            // if the command is
                            // /launchpad setpad <xpower> <ypower> <zpower>
                            // set a new pad to the player's location
                            // with the power values provided
                            else if (args.length == 4) {

                                Location playerLocation = player.getLocation();

                                int padNumber = padCoords.getInt("numberOfPads") + 1;
                                int playerX = playerLocation.getBlockX();
                                int playerY = playerLocation.getBlockY();
                                int playerZ = playerLocation.getBlockZ();
                                double padXPower = Double.parseDouble(args[1]);
                                double padYPower = Double.parseDouble(args[2]);
                                double padZPower = Double.parseDouble(args[3]);

                                // check if there is already a pad in that location
                                for (int i = 1; i < numberOfPads + 1; i++) {

                                    if (padCoords.getString("" + i + ".world").equals(world.getName())) {

                                        if (padCoords.getInt("" + i + ".x") == playerX) {

                                            if (padCoords.getInt("" + i + ".z") == playerZ) {

                                                if (padCoords.getInt("" + i + ".y") == playerY) {

                                                    // there's a pad there, so let the player know
                                                    // and stop the rest of the command from happening
                                                    sender.sendMessage(Component.text("[LaunchPad]")
                                                            .color(TextColor.color(0, 255, 0))
                                                            .append(Component.text(" There's Already a Pad There!")
                                                                    .color(TextColor.color(255, 0, 0))));
                                                    return true;

                                                }

                                            }

                                        }

                                    }

                                }

                                padCoords.set(padNumber + ".world", player.getWorld().getName());
                                padCoords.set(padNumber + ".x", playerX);
                                padCoords.set(padNumber + ".y", playerY);
                                padCoords.set(padNumber + ".z", playerZ);
                                padCoords.set(padNumber + ".xpower", padXPower);
                                padCoords.set(padNumber + ".ypower", padYPower);
                                padCoords.set(padNumber + ".zpower", padZPower);
                                // this makes it easier to search the config file
                                // use CTRL+F and search for the coords of the pad
                                padCoords.set(padNumber + ".searchID", playerLocation.getBlockX() + " "
                                        + playerLocation.getBlockY() + " " + playerLocation.getBlockZ());

                                padCoords.set("numberOfPads", padNumber);

                                LaunchPadsOG.getPlugin(LaunchPadsOG.class).saveConfig();

                                // tell the player that the pad has been set at their coordinates with the given
                                // powers
                                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0)) // green
                                        .append(Component
                                                .text(" Pad Set at: " + playerX + ", " + playerY + ", " + playerZ)
                                                .color(TextColor.color(255, 255, 0))) // yellow
                                        .append(Component.text(" With Powers (x, y, z): " + padXPower + ", " + padYPower
                                                + ", " + padZPower).color(TextColor.color(0, 191, 255)))); // light blue

                            }
                            // if the command is
                            // /launchpad setpad <x> <y> <z> <xpower> <ypower> <zpower>
                            // set a new pad to the provided location
                            // in the player's world
                            // with the power values provided
                            else if (args.length == 7) {

                                int padNumber = padCoords.getInt("numberOfPads") + 1;
                                int padX = Integer.parseInt(args[1]);
                                int padY = Integer.parseInt(args[2]);
                                int padZ = Integer.parseInt(args[3]);
                                double padXPower = Double.parseDouble(args[4]);
                                double padYPower = Double.parseDouble(args[5]);
                                double padZPower = Double.parseDouble(args[6]);

                                // check if there is already a pad in that location
                                for (int i = 1; i < numberOfPads + 1; i++) {

                                    if (padCoords.getString("" + i + ".world").equals(world.getName())) {

                                        if (padCoords.getInt("" + i + ".x") == padX) {

                                            if (padCoords.getInt("" + i + ".z") == padZ) {

                                                if (padCoords.getInt("" + i + ".y") == padY) {

                                                    // there's a pad there, so let the player know
                                                    // and stop the rest of the command from happening
                                                    sender.sendMessage(Component.text("[LaunchPad]")
                                                            .color(TextColor.color(0, 255, 0))
                                                            .append(Component.text(" There's Already a Pad There!")
                                                                    .color(TextColor.color(255, 0, 0))));
                                                    return true;

                                                }

                                            }

                                        }

                                    }

                                }

                                padCoords.set(padNumber + ".world", player.getWorld().getName());
                                padCoords.set(padNumber + ".x", padX);
                                padCoords.set(padNumber + ".y", padY);
                                padCoords.set(padNumber + ".z", padZ);
                                padCoords.set(padNumber + ".xpower", padXPower);
                                padCoords.set(padNumber + ".ypower", padYPower);
                                padCoords.set(padNumber + ".zpower", padZPower);
                                // this makes it easier to search the config file
                                // use CTRL+F and search for the coords of the pad
                                padCoords.set(padNumber + ".searchID", padX + " " + padY + " " + padZ);

                                padCoords.set("numberOfPads", padNumber);

                                LaunchPadsOG.getPlugin(LaunchPadsOG.class).saveConfig();

                                // tell the player that the pad has been set at their coordinates with the given
                                // powers
                                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0)) // green
                                        .append(Component.text(" Pad Set at: " + padX + ", " + padY + ", " + padZ)
                                                .color(TextColor.color(255, 255, 0))) // yellow
                                        .append(Component.text(" With Powers (x, y, z): " + padXPower + ", " + padYPower
                                                + ", " + padZPower).color(TextColor.color(0, 191, 255)))); // light blue

                            }

                        }
                        // if the player doesn't have permission to use this command,
                        // let them know
                        else {

                            sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                    .append(Component.text(" You Don't Have Permission to do That!")
                                            .color(TextColor.color(255, 0, 0))));

                        }

                    }

                    // if the command is /launchpad reloadconfig
                    else if (args[0].equalsIgnoreCase("reloadconfig")) {

                        if (sender.hasPermission("launchpad.canreload")) {

                            FileConfiguration oldPadCoords = LaunchPadsOG.getConfiguration();
                            LaunchPadsOG.getPlugin(LaunchPadsOG.class).reloadConfig();
                            padCoords = LaunchPadsOG.getConfiguration();

                            // check through the config to make sure that
                            // there are as many launchpads as numberOfPads
                            // if there is not, move all of the pad number down
                            // by one starting at the missing number

                            int maxNumberOfPads = numberOfPads + 1;
                            if (numberOfPads > 1) {

                                for (int i = 1; i < maxNumberOfPads; i++) {

                                    // if the world is null, then the pad is not set
                                    if (padCoords.getString(i + ".world") == null) {

                                        // move all other pads up one id number
                                        for (int j = i; j < padCoords.getInt("numberOfPads") + 1; j++) {

                                            padCoords.set("" + j + ".x", padCoords.getInt("" + (j + 1) + ".x"));
                                            padCoords.set("" + j + ".y", padCoords.getInt("" + (j + 1) + ".y"));
                                            padCoords.set("" + j + ".z", padCoords.getInt("" + (j + 1) + ".z"));
                                            padCoords.set("" + j + ".world",
                                                    padCoords.getString("" + (j + 1) + ".world"));
                                            padCoords.set("" + j + ".xpower",
                                                    padCoords.getDouble("" + (j + 1) + ".xpower"));
                                            padCoords.set("" + j + ".ypower",
                                                    padCoords.getDouble("" + (j + 1) + ".ypower"));
                                            padCoords.set("" + j + ".zpower",
                                                    padCoords.getDouble("" + (j + 1) + ".zpower"));
                                            // this makes it easier to search the config file
                                            // use CTRL+F and search for the coords of the pad
                                            padCoords.set("" + j + ".searchID",
                                                    padCoords.getString("" + (j + 1) + ".searchID"));

                                        }

                                        padCoords.set("" + numberOfPads, null);
                                        padCoords.set("numberOfPads", --numberOfPads);

                                    }

                                }

                                // check if the last pad exists
                                if (padCoords.getString(numberOfPads + ".world") == null) {

                                    // decrement numberOfPads by one
                                    padCoords.set("numberOfPads", --numberOfPads);

                                }

                            } else {

                                // if numberOfPads is 1, then there can only be one pad
                                // check if it exists. If not, set numberOfPads to 0
                                if (padCoords.getString("1.world") == null) {

                                    padCoords.set("numberOfPads", 0);

                                }

                            }

                            // check if numberOfPads is below 0
                            // if so, set it to 0
                            if (padCoords.getInt("numberOfPads") < 0) {

                                padCoords.set("numberOfPads", 0);

                            }

                            /* check that everything has what it is supposed to have */

                            // check that numberOfPads is an integer
                            if (!(padCoords.get("numberOfPads") instanceof Integer)) {

                                // tell the player that something is wrong with numberOfPads
                                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                        .append(Component.text(" numberOfPads has to be an integer")
                                                .color(TextColor.color(255, 0, 0))));
                                // reset padConfig to the pre-reload config
                                restoreOldConfig(oldPadCoords);
                                return true;

                            }
                            // check that numberOfPads is greater than or equal to 0
                            else if (padCoords.getInt("numberOfPads") < 0) {

                                // tell the player that numberOfPads has to be greater than 0
                                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                        .append(Component.text(" numberOfPads has to be greater than or equal to 0")
                                                .color(TextColor.color(255, 0, 0))));
                                // reset padConfig to the pre-reload config
                                restoreOldConfig(oldPadCoords);
                                return true;

                            }

                            // check that cancraft is a boolean
                            if (!(padCoords.get("cancraft") instanceof Boolean)) {

                                // tell the player that the cancraft needs to be an boolean
                                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                        .append(Component.text(" cancraft must be a boolean")
                                                .color(TextColor.color(255, 0, 0))));
                                // reset padConfig to the pre-reload config
                                restoreOldConfig(oldPadCoords);
                                return true;

                            }

                            // check that the default pad powers are doubles
                            if (!(padCoords.get("defaultXpower") instanceof Double)) {

                                // tell the player that the defaultXpower needs to be a double
                                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                        .append(Component.text(" defaultXpower must be a double")
                                                .color(TextColor.color(255, 0, 0))));
                                // reset padConfig to the pre-reload config
                                restoreOldConfig(oldPadCoords);
                                return true;

                            }

                            if (!(padCoords.get("defaultYpower") instanceof Double)) {

                                // tell the player that the defaultYpower needs to be a double
                                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                        .append(Component.text(" defaultYpower must be a double")
                                                .color(TextColor.color(255, 0, 0))));
                                // reset padConfig to the pre-reload config
                                restoreOldConfig(oldPadCoords);
                                return true;

                            }

                            if (!(padCoords.get("defaultZpower") instanceof Double)) {

                                // tell the player that the defaultZpower needs to be a double
                                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                        .append(Component.text(" defaultZpower must be a double")
                                                .color(TextColor.color(255, 0, 0))));
                                // reset padConfig to the pre-reload config
                                restoreOldConfig(oldPadCoords);
                                return true;

                            }

                            // check that idleParticleCount is an integer
                            if (!(padCoords.get("idleParticleCount") instanceof Integer)) {

                                // tell the player that the idleParticleCount needs to be an integer
                                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                        .append(Component.text(" idleParticleCount must be an integer")
                                                .color(TextColor.color(255, 0, 0))));
                                // reset padConfig to the pre-reload config
                                restoreOldConfig(oldPadCoords);
                                return true;

                            }

                            // check that idleParticlePower is a double
                            if (!(padCoords.get("idleParticlePower") instanceof Double)) {

                                // tell the player that the idleParticlePower needs to be a double
                                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                        .append(Component.text(" idleParticlePower must be a double")
                                                .color(TextColor.color(255, 0, 0))));
                                // reset padConfig to the pre-reload config
                                restoreOldConfig(oldPadCoords);
                                return true;

                            }

                            // check that padLaunchParticleCount is an integer
                            if (!(padCoords.get("padLaunchParticleCount") instanceof Integer)) {

                                // tell the player that the padLaunchParticleCount needs to be an integer
                                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                        .append(Component.text(" padLaunchParticleCount must be an integer")
                                                .color(TextColor.color(255, 0, 0))));
                                // reset padConfig to the pre-reload config
                                restoreOldConfig(oldPadCoords);
                                return true;

                            }

                            // check that padLaunchParticlePower is a double
                            if (!(padCoords.get("padLaunchParticlePower") instanceof Double)) {

                                // tell the player that the padLaunchParticlePower needs to be a double
                                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                        .append(Component.text(" padLaunchParticlePower must be a double")
                                                .color(TextColor.color(255, 0, 0))));
                                // reset padConfig to the pre-reload config
                                restoreOldConfig(oldPadCoords);
                                return true;

                            }

                            // check that playerLaunchParticleCountMultipier is a double
                            if (!(padCoords.get("playerLaunchParticleCountMultipier") instanceof Double)) {

                                // tell the player that the playerLaunchParticleCountMultipier needs to be a
                                // double
                                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                        .append(Component.text(" playerLaunchParticleCountMultipier must be a double")
                                                .color(TextColor.color(255, 0, 0))));
                                // reset padConfig to the pre-reload config
                                restoreOldConfig(oldPadCoords);
                                return true;

                            }

                            // check that every world is a string, every x, y, z is an integer,
                            // and every xpower, ypower, zpower are doubles
                            for (int i = 1; i < numberOfPads + 1; i++) {

                                // check if world is set to a string
                                if (padCoords.get(i + ".world") instanceof String) {

                                    // check if world is set to a valid name
                                    if (!(padCoords.getString(i + ".world").equals("world")
                                            || padCoords.getString(i + ".world").equals("world_nether")
                                            || padCoords.getString(i + ".world").equals("world_the_end")))
                                    {

                                        // if not, tell the player that it has to be one of the above
                                        sender.sendMessage(Component.text("[LaunchPad]")
                                                .color(TextColor.color(0, 255, 0))
                                                .append(Component
                                                        .text(" world of pad ID " + i + " must be either "
                                                                + "\"world\", \"world_nether\", or \"world_the_end\" ")
                                                        .color(TextColor.color(255, 0, 0))));
                                        // reset padConfig to the pre-reload config
                                        restoreOldConfig(oldPadCoords);
                                        return true;

                                    }

                                } else {

                                    // tell the player that the world has to be a string
                                    sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                            .append(Component.text(" world of pad ID " + i + " must be a string")
                                                    .color(TextColor.color(255, 0, 0))));
                                    // reset padConfig to the pre-reload config
                                    restoreOldConfig(oldPadCoords);
                                    return true;

                                }

                                // check that x,y,z are integers
                                if (!(padCoords.get(i + ".x") instanceof Integer)) {

                                    // tell the player that the x needs to be an integer
                                    sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                            .append(Component.text(" x of pad ID " + i + " must be an integer")
                                                    .color(TextColor.color(255, 0, 0))));
                                    // reset padConfig to the pre-reload config
                                    restoreOldConfig(oldPadCoords);
                                    return true;

                                }

                                if (!(padCoords.get(i + ".y") instanceof Integer)) {

                                    // tell the player that the y needs to be an integer
                                    sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                            .append(Component.text(" y of pad ID " + i + " must be an integer")
                                                    .color(TextColor.color(255, 0, 0))));
                                    // reset padConfig to the pre-reload config
                                    restoreOldConfig(oldPadCoords);
                                    return true;

                                }

                                if (!(padCoords.get(i + ".z") instanceof Integer)) {

                                    // tell the player that the z needs to be an integer
                                    sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                            .append(Component.text(" z of pad ID " + i + " must be an integer")
                                                    .color(TextColor.color(255, 0, 0))));
                                    // reset padConfig to the pre-reload config
                                    restoreOldConfig(oldPadCoords);
                                    return true;

                                }

                                // check if xpower, ypower, zpower are doubles
                                if (!(padCoords.get(i + ".xpower") instanceof Double)) {

                                    // tell the player that the x needs to be an integer
                                    sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                            .append(Component.text(" xpower of pad ID " + i + " must be a double")
                                                    .color(TextColor.color(255, 0, 0))));
                                    // reset padConfig to the pre-reload config
                                    restoreOldConfig(oldPadCoords);
                                    return true;

                                }

                                if (!(padCoords.get(i + ".ypower") instanceof Double)) {

                                    // tell the player that the y needs to be an integer
                                    sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                            .append(Component.text(" ypower of pad ID " + i + " must be a double")
                                                    .color(TextColor.color(255, 0, 0))));
                                    // reset padConfig to the pre-reload config
                                    restoreOldConfig(oldPadCoords);
                                    return true;

                                }

                                if (!(padCoords.get(i + ".zpower") instanceof Double)) {

                                    // tell the player that the z needs to be an integer
                                    sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                            .append(Component.text(" zpower of pad ID " + i + " must be a double")
                                                    .color(TextColor.color(255, 0, 0))));
                                    // reset padConfig to the pre-reload config
                                    restoreOldConfig(oldPadCoords);
                                    return true;

                                }

                            }

                            // save and reload the config
                            LaunchPadsOG.getPlugin(LaunchPadsOG.class).saveConfig();
                            LaunchPadsOG.getPlugin(LaunchPadsOG.class).reloadConfig();
                            padCoords = LaunchPadsOG.getConfiguration();

                            // tell the player that ran the command that the plugin config has been reloaded
                            sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                    .append(Component.text(" Config Reloaded").color(TextColor.color(255, 255, 0))));

                        }
                        // if the player doesn't have permission to use this command,
                        // let them know
                        else {

                            sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                    .append(Component.text(" You Don't Have Permission to do That!")
                                            .color(TextColor.color(255, 0, 0))));

                        }

                    }

                    // if the command is /launchpad give
                    else if (args[0].equalsIgnoreCase("give")) {

                        // if it's just /givepad
                        if (args.length == 1) {

                            if (player.hasPermission("launchpad.cangive")) {

                                // create the launchpad item
                                Vector<Component> loreList = new java.util.Vector<>();

                                ItemStack launchPad = new ItemStack(Material.STONE_PRESSURE_PLATE);
                                ItemMeta padMeta = launchPad.getItemMeta();

                                padMeta.displayName(Component.text("Launch Pad").decorate(TextDecoration.ITALIC));

                                loreList.add(Component.text("Launches you in the"));
                                loreList.add(Component.text("direction you are facing"));
                                padMeta.lore(Collections.list(loreList.elements()));
                                // add a useless enchantment to the item to make it
                                // stand out in the inventory
                                padMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
                                launchPad.setItemMeta(padMeta);

                                // give the launchpad item to the player
                                player.getInventory().addItem(launchPad);

                                // let the player know that a launchpad was given
                                sender.sendMessage(
                                        Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0)).append(Component
                                                .text(" Launch Pad Given").color(TextColor.color(255, 255, 0))));

                            }
                            // if the player doesn't have permission to use this command,
                            // let them know
                            else {

                                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                        .append(Component.text(" You Don't Have Permission to do That!")
                                                .color(TextColor.color(255, 0, 0))));

                            }

                        }

                    }

                    // if the command is /launchpad delete
                    else if (args[0].equalsIgnoreCase("delete")) {

                        if (player.hasPermission("launchpad.candelete")) {

                            // if it's just /launchpad delpad
                            if (args.length == 1) {

                                // delete the pad that is in the block above the block that
                                // the player is looking at within a distance of 5 blocks
                                Block block = player.getTargetBlockExact(4);
                                if (block == null) {

                                    // tell the player that they are not looking at a block
                                    sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                            .append(Component.text(" No Block Targeted")
                                                    .color(TextColor.color(255, 0, 0))));
                                    return true;

                                }

                                // check if there is a launchpad at the coords
                                // above the block targeted
                                boolean found = false;
                                for (int i = 1; i < numberOfPads + 1; i++) {

                                    if (padCoords.getString("" + i + ".world").equals(world.getName())) {

                                        if (padCoords.getInt("" + i + ".x") == block.getX()) {

                                            if (padCoords.getInt("" + i + ".z") == block.getZ()) {

                                                // check the block above and the block that the player is
                                                // aiming at. The pad is in the block they are aiming at
                                                // if the launchpad was placed with the item
                                                if (padCoords.getInt("" + i + ".y") == block.getY() + 1
                                                        || padCoords.getInt("" + i + ".y") == block.getY())
                                                {

                                                    int padX = padCoords.getInt(i + ".x");
                                                    int padY = padCoords.getInt(i + ".y");
                                                    int padZ = padCoords.getInt(i + ".z");
                                                    // there is a pad, so remove it and
                                                    // move all other pad id's up one
                                                    for (int j = i; j < padCoords.getInt("numberOfPads") + 1; j++) {

                                                        padCoords.set("" + j + ".x",
                                                                padCoords.getInt("" + (j + 1) + ".x"));
                                                        padCoords.set("" + j + ".y",
                                                                padCoords.getInt("" + (j + 1) + ".y"));
                                                        padCoords.set("" + j + ".z",
                                                                padCoords.getInt("" + (j + 1) + ".z"));
                                                        padCoords.set("" + j + ".world",
                                                                padCoords.getString("" + (j + 1) + ".world"));
                                                        padCoords.set("" + j + ".xpower",
                                                                padCoords.getDouble("" + (j + 1) + ".xpower"));
                                                        padCoords.set("" + j + ".ypower",
                                                                padCoords.getDouble("" + (j + 1) + ".ypower"));
                                                        padCoords.set("" + j + ".zpower",
                                                                padCoords.getDouble("" + (j + 1) + ".zpower"));
                                                        // this makes it easier to search the config file
                                                        // use CTRL+F and search for the coords of the pad
                                                        padCoords.set("" + j + ".searchID",
                                                                padCoords.getString("" + (j + 1) + ".searchID"));

                                                    }

                                                    padCoords.set("" + numberOfPads, null);
                                                    padCoords.set("numberOfPads", numberOfPads - 1);
                                                    LaunchPadsOG.getPlugin(LaunchPadsOG.class).saveConfig();

                                                    found = true;

                                                    // let the player know that the launchpad was removed
                                                    sender.sendMessage(Component.text("[LaunchPad]")
                                                            .color(TextColor.color(0, 255, 0))
                                                            .append(Component
                                                                    .text(" Launch Pad Removed From: " + padX + ", "
                                                                            + padY + ", " + padZ)
                                                                    .color(TextColor.color(255, 255, 0))));

                                                    return true;

                                                }

                                            }

                                        }

                                    }

                                }

                                // if no launchpad was found at that location, let the player know
                                if (!found) {

                                    sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                            .append(Component.text(" No Pad Found").color(TextColor.color(255, 0, 0))));

                                }

                            }
                            // if it's /launchpad delete <x> <y> <z>
                            else if (args.length == 4) {

                                // check for a launchpad at the coords given in the player's world
                                int blockX = Integer.parseInt(args[1]);
                                int blockY = Integer.parseInt(args[2]);
                                int blockZ = Integer.parseInt(args[3]);

                                boolean found = false;
                                for (int i = 1; i < numberOfPads + 1; i++) {

                                    if (padCoords.getString("" + i + ".world").equals(world.getName())) {

                                        if (padCoords.getInt("" + i + ".x") == blockX) {

                                            if (padCoords.getInt("" + i + ".z") == blockZ) {

                                                if (padCoords.getInt("" + i + ".y") == blockY) {

                                                    // there is a pad, so remove it and
                                                    // move all other pad id's up one
                                                    for (int j = i; j < padCoords.getInt("numberOfPads") + 1; j++) {

                                                        padCoords.set("" + j + ".x",
                                                                padCoords.getInt("" + (j + 1) + ".x"));
                                                        padCoords.set("" + j + ".y",
                                                                padCoords.getInt("" + (j + 1) + ".y"));
                                                        padCoords.set("" + j + ".z",
                                                                padCoords.getInt("" + (j + 1) + ".z"));
                                                        padCoords.set("" + j + ".world",
                                                                padCoords.getString("" + (j + 1) + ".world"));
                                                        padCoords.set("" + j + ".xpower",
                                                                padCoords.getDouble("" + (j + 1) + ".xpower"));
                                                        padCoords.set("" + j + ".ypower",
                                                                padCoords.getDouble("" + (j + 1) + ".ypower"));
                                                        padCoords.set("" + j + ".zpower",
                                                                padCoords.getDouble("" + (j + 1) + ".zpower"));
                                                        // this makes it easier to search the config file
                                                        // use CTRL+F and search for the coords of the pad
                                                        padCoords.set("" + j + ".searchID",
                                                                padCoords.getString("" + (j + 1) + ".searchID"));

                                                    }

                                                    padCoords.set("" + numberOfPads, null);
                                                    padCoords.set("numberOfPads", numberOfPads - 1);
                                                    LaunchPadsOG.getPlugin(LaunchPadsOG.class).saveConfig();

                                                    found = true;

                                                    // let the player know that the launchpad was removed
                                                    sender.sendMessage(Component.text("[LaunchPad]")
                                                            .color(TextColor.color(0, 255, 0))
                                                            .append(Component
                                                                    .text(" Launch Pad Removed From: " + blockX + ", "
                                                                            + blockY + ", " + blockZ)
                                                                    .color(TextColor.color(255, 255, 0))));

                                                }

                                            }

                                        }

                                    }

                                }

                                // if no launchpad was found at that location, let the player know
                                if (!found) {

                                    sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                            .append(Component.text(" No Pad Found").color(TextColor.color(255, 0, 0))));

                                }

                            }
                            // if it's /launchpad delete <world> <x> <y> <z>
                            else if (args.length == 5) {

                                // check of a launchpad with the given coords
                                // allow the names overworld, nether, and end to be used
                                String worldName = null;
                                if (args[1].equalsIgnoreCase("overworld")) {

                                    worldName = "world";

                                } else if (args[1].equalsIgnoreCase("nether")) {

                                    worldName = "world_nether";

                                } else if (args[1].equalsIgnoreCase("end")) {

                                    worldName = "world_the_end";

                                } else {

                                    worldName = args[1];

                                }

                                int blockX = Integer.parseInt(args[2]);
                                int blockY = Integer.parseInt(args[3]);
                                int blockZ = Integer.parseInt(args[4]);

                                boolean found = false;
                                for (int i = 1; i < numberOfPads + 1; i++) {

                                    if (padCoords.getString("" + i + ".world").equals(worldName)) {

                                        if (padCoords.getInt("" + i + ".x") == blockX) {

                                            if (padCoords.getInt("" + i + ".z") == blockZ) {

                                                if (padCoords.getInt("" + i + ".y") == blockY) {

                                                    // there is a pad, so remove it and
                                                    // move all other pad id's up one
                                                    for (int j = i; j < padCoords.getInt("numberOfPads") + 1; j++) {

                                                        padCoords.set("" + j + ".x",
                                                                padCoords.getInt("" + (j + 1) + ".x"));
                                                        padCoords.set("" + j + ".y",
                                                                padCoords.getInt("" + (j + 1) + ".y"));
                                                        padCoords.set("" + j + ".z",
                                                                padCoords.getInt("" + (j + 1) + ".z"));
                                                        padCoords.set("" + j + ".world",
                                                                padCoords.getString("" + (j + 1) + ".world"));
                                                        padCoords.set("" + j + ".xpower",
                                                                padCoords.getDouble("" + (j + 1) + ".xpower"));
                                                        padCoords.set("" + j + ".ypower",
                                                                padCoords.getDouble("" + (j + 1) + ".ypower"));
                                                        padCoords.set("" + j + ".zpower",
                                                                padCoords.getDouble("" + (j + 1) + ".zpower"));
                                                        // this makes it easier to search the config file
                                                        // use CTRL+F and search for the coords of the pad
                                                        padCoords.set("" + j + ".searchID",
                                                                padCoords.getString("" + (j + 1) + ".searchID"));

                                                    }

                                                    padCoords.set("" + numberOfPads, null);
                                                    padCoords.set("numberOfPads", numberOfPads - 1);
                                                    LaunchPadsOG.getPlugin(LaunchPadsOG.class).saveConfig();

                                                    found = true;

                                                    // let the player know that the launchpad was removed
                                                    sender.sendMessage(Component.text("[LaunchPad]")
                                                            .color(TextColor.color(0, 255, 0))
                                                            .append(Component
                                                                    .text(" Launch Pad Removed From: " + blockX + ", "
                                                                            + blockY + ", " + blockZ)
                                                                    .color(TextColor.color(255, 255, 0))));

                                                }

                                            }

                                        }

                                    }

                                }

                                // if no launchpad was found at that location, let the player know
                                if (!found) {

                                    sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                            .append(Component.text(" No Pad Found").color(TextColor.color(255, 0, 0))));

                                }

                            }

                        }
                        // if the player doesn't have permission to use this command,
                        // let them know
                        else {

                            sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                    .append(Component.text(" You Don't Have Permission to do That!")
                                            .color(TextColor.color(255, 0, 0))));

                        }

                    }

                    // if the command is /launchpad help
                    else if (args[0].equalsIgnoreCase("help")) {

                        var mm = MiniMessage.miniMessage();

                        // Component parsed = mm.deserialize(
                        // // create a separator line
                        // "-----------------------------------------------" +
                        // // tell the player about /launchpad give
                        // "\n<color:#0080ff>/launchpad give" +
                        // "\n<color:white>Give yourself a launchpad" +
                        // // tell the player about /launchpad set
                        // "\n\n<color:#0080ff>/launchpad set" +
                        // "\n<color:white>Set a launchpad at your feet" +
                        // "\n\n<color:#0080ff>/launchpad set <color:#00ffff><xpower>
                        // <ypower> <zpower>" +
                        // "\n<color:white>Set a launchpad at your feet with those
                        // powers" +
                        // "\n\n<color:#0080ff>/launchpad set <color:#00ffff><x> <y> <z>
                        // <xpower> <ypower> <zpower>" +
                        // "\n<color:white>Set a launchpad there with those powers" +
                        // // tell the player about /launchpad delete
                        // "\n\n<color:#0080ff>/launchpad delete" +
                        // "\n<color:white>Delete the launchpad on the block you are
                        // pointing at" +
                        // "\n\n<color:#0080ff>/launchpad delete <color:#00ffff><x> <y>
                        // <z>" +
                        // "\n<color:white>Delete the launchpad from that block" +
                        // "\n\n<color:#0080ff>/launchpad delete <color:#00ffff><world>
                        // <x> <y> <z>" +
                        // "\n<color:white>Delete the launchpad from that block in that
                        // world" +
                        // // tell the player about /launchpad reloadconfig
                        // "\n\n<color:#0080ff>/launchpad reloadconfig" +
                        // "\n<color:white>Reload the configuration file after a change"
                        // +
                        // // tell the player about /launchpad help
                        // "\n\n<color:#0080ff>/launchpad help" +
                        // "\nDisplay this page");

                        // yellow ----
                        // gold Help: LaunchPad
                        // yellow --
                        // gold Page: 1/2
                        // yellow ----
                        // gold /launchpad ...
                        // white description

                        // if the player asked for the home page of help
                        if (args.length == 1) {

                            // display the home page of help
                            Component parsed = mm.deserialize(
                                    // The top of the page
                                    "<color:yellow>---- <color:gold>Help: LaunchPad <color:yellow>----"
                                            + "\n<color:gold>Commands from LaunchPads-OG:"
                                            + "\n/launchpad help set: <color:white>Display detailed help for set"
                                            + "\n<color:gold>/launchpad help delete: <color:white>Display detailed help for delete"
                                            + "\n<color:gold>/launchpad reloadconfig: <color:white>Reload the plugin configuration"
                                            + "\n<color:gold>/launchpad give: <color:white>Give yourself a launchpad item"
                                            + "\n<color:gold>/launchpad help: <color:white>Display this page");

                            sender.sendMessage(parsed);

                        } else {

                            // check which command the player asked for help with
                            // if they asked for set
                            if (args[1].equalsIgnoreCase("set")) {

                                Component parsed = mm.deserialize(
                                        // The top of the page
                                        "<color:yellow>---- <color:gold>Help: /launchpad set <color:yellow>----" +
                                        // "\n<color:gold>Help for /launchpad
                                        // set:" +
                                                "\n<color:gold>/launchpad set: <color:white>Set a launchpad at your feet with the default power values"
                                                + "\n<color:gold>/launchpad set <color:red>[xpower] [ypower] [zpower]: <color:white>Set a launchpad at your feet with those powers"
                                                + "\n<color:gold>/launchpad set <color:red>[x] [y] [z] [xpower] [ypower] [zpower]: <color:white>Set a launchpad at those coordinates with those powers");

                                sender.sendMessage(parsed);

                            }
                            // if they asked for delete
                            else if (args[1].equalsIgnoreCase("delete")) {

                                Component parsed = mm.deserialize(
                                        // The top of the page
                                        "<color:yellow>---- <color:gold>Help: /launchpad delete <color:yellow>----" +
                                        // "\n<color:gold>Help for /launchpad
                                        // delete:" +
                                                "\n<color:gold>/launchpad delete: <color:white>Delete the launchpad in or above the block you are looking at"
                                                + "\n<color:gold>/launchpad delete <color:red>[x] [y] [z]: <color:white>Delete the launchpad at those coordinates"
                                                + "\n<color:gold>/launchpad delete <color:red><world> [x] [y] [z]: <color:white>Delete the launchpad at those coordinates in that world");

                                sender.sendMessage(parsed);

                            }

                        }

                    }

                    // the player did not input an acceptable second argument
                    else {

                        // tell the player that that command doesn't exist
                        sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                                .append(Component.text(" Command ").color(TextColor.color(255, 0, 0)))
                                .append(Component.text("/" + command.getName() + " " + args[0])
                                        .color(TextColor.color(255, 75, 75)))
                                .append(Component.text(" does not exist").color(TextColor.color(255, 0, 0))));

                    }

                }

            } else {

                // tell the player that they need to provide a command
                sender.sendMessage(Component.text("[LaunchPad]").color(TextColor.color(0, 255, 0))
                        .append(Component.text(" Please provide a command").color(TextColor.color(255, 0, 0))));

            }

        }

        return true;

    }

    private void restoreOldConfig(FileConfiguration oldPadCoords) {

        try {

            File configFile = new File(LaunchPadsOG.getPlugin(LaunchPadsOG.class).getDataFolder(), "config.yml");
            oldPadCoords.save(configFile);
            LaunchPadsOG.getPlugin(LaunchPadsOG.class).reloadConfig();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}
