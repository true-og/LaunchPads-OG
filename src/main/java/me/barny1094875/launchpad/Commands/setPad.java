package me.barny1094875.launchpad.Commands;

import me.barny1094875.launchpad.LaunchPad;
import me.barny1094875.launchpad.config.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class setPad implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

//        if(command.getName().equalsIgnoreCase("setPad")) {
        if(command.getName().equalsIgnoreCase("launchpad")) {
            if(args[0].equalsIgnoreCase("setpad")) {
                if (sender.hasPermission("launchpad.canset")) {
                    // if the command is just /launchpad setpad
                    // set a new pad to the player's location
                    // and set the power to the default values
                    if (args.length == 1) {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            Location playerLocation = player.getLocation();

                            Config padConfig = LaunchPad.config();
                            FileConfiguration padCoords = padConfig.getConfig();
                            int padNumber = padCoords.getInt("numberOfPads") + 1;
                            int playerX = playerLocation.getBlockX();
                            int playerY = playerLocation.getBlockY();
                            int playerZ = playerLocation.getBlockZ();

                            padCoords.set(padNumber + ".world", player.getWorld().getName());
                            padCoords.set(padNumber + ".x", playerX);
                            padCoords.set(padNumber + ".y", playerY);
                            padCoords.set(padNumber + ".z", playerZ);
                            padCoords.set(padNumber + ".xpower", padCoords.getInt("defaultXpower"));
                            padCoords.set(padNumber + ".ypower", padCoords.getInt("defaultYpower"));
                            padCoords.set(padNumber + ".zpower", padCoords.getInt("defaultZpower"));
                            // this makes it easier to search the config file
                            // use CTRL+F and search for the coords of the pad
                            padCoords.set(padNumber + ".searchID", playerX + " " + playerY + " " + playerZ);

                            padCoords.set("numberOfPads", padNumber);

                            padConfig.save();

                            // tell the player that the pad has been set at their coordinates
                            sender.sendMessage(Component.text("[LaunchPad]")
                                    .color(TextColor.color(0, 255, 0))
                                    .append(Component.text(" Pad Set at: " + playerX + ", " + playerY + ", " + playerZ)
                                            .color(TextColor.color(255, 255, 0))));
                        }
                    }
                    // if the command is
                    // /launchpad setpad <xpower> <ypower> <zpower>
                    // set a new pad to the player's location
                    // with the power values provided
                    else if (args.length == 4) {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            Location playerLocation = player.getLocation();

                            Config padConfig = LaunchPad.config();
                            FileConfiguration padCoords = padConfig.getConfig();
                            int padNumber = padCoords.getInt("numberOfPads") + 1;
                            int playerX = playerLocation.getBlockX();
                            int playerY = playerLocation.getBlockY();
                            int playerZ = playerLocation.getBlockZ();
                            int padXPower = Integer.valueOf(args[1]);
                            int padYPower = Integer.valueOf(args[2]);
                            int padZPower = Integer.valueOf(args[3]);

                            padCoords.set(padNumber + ".world", player.getWorld().getName());
                            padCoords.set(padNumber + ".x", playerX);
                            padCoords.set(padNumber + ".y", playerY);
                            padCoords.set(padNumber + ".z", playerZ);
                            padCoords.set(padNumber + ".xpower", padXPower);
                            padCoords.set(padNumber + ".ypower", padYPower);
                            padCoords.set(padNumber + ".zpower", padZPower);
                            // this makes it easier to search the config file
                            // use CTRL+F and search for the coords of the pad
                            padCoords.set(padNumber + ".searchID", playerLocation.getBlockX() + " " + playerLocation.getBlockY() + " " + playerLocation.getBlockZ());

                            padCoords.set("numberOfPads", padNumber);

                            padConfig.save();


                            // tell the player that the pad has been set at their coordinates with the given powers
                            sender.sendMessage(Component.text("[LaunchPad]")
                                    .color(TextColor.color(0, 255, 0)) // green
                                    .append(Component.text(" Pad Set at: " + playerX + ", " + playerY + ", " + playerZ)
                                            .color(TextColor.color(255, 255, 0))) // yellow
                                    .append(Component.text(" With Powers (x, y, z): " + padXPower + ", " + padYPower + ", " + padZPower)
                                            .color(TextColor.color(0, 191, 255)))); // light blue
                        }
                    }
                    // if the command is
                    // /launchpad setpad <x> <y> <z> <xpower> <ypower> <zpower>
                    // set a new pad to the provided location
                    // in the player's world
                    // with the power values provided
                    else if (args.length == 7) {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            Location playerLocation = player.getLocation();

                            Config padConfig = LaunchPad.config();
                            FileConfiguration padCoords = padConfig.getConfig();
                            int padNumber = padCoords.getInt("numberOfPads") + 1;
                            int padX = Integer.valueOf(args[1]);
                            int padY = Integer.valueOf(args[2]);
                            int padZ = Integer.valueOf(args[3]);
                            int padXPower = Integer.valueOf(args[4]);
                            int padYPower = Integer.valueOf(args[5]);
                            int padZPower = Integer.valueOf(args[6]);

                            padCoords.set(padNumber + ".world", player.getWorld().getName());
                            padCoords.set(padNumber + ".x", padX);
                            padCoords.set(padNumber + ".y", padY);
                            padCoords.set(padNumber + ".z", padZ);
                            padCoords.set(padNumber + ".xpower", padXPower);
                            padCoords.set(padNumber + ".ypower", padYPower);
                            padCoords.set(padNumber + ".zpower", padZPower);
                            // this makes it easier to search the config file
                            // use CTRL+F and search for the coords of the pad
                            padCoords.set(padNumber + ".searchID", playerLocation.getBlockX() + " " + playerLocation.getBlockY() + " " + playerLocation.getBlockZ());

                            padCoords.set("numberOfPads", padNumber);

                            padConfig.save();


                            // tell the player that the pad has been set at their coordinates with the given powers
                            sender.sendMessage(Component.text("[LaunchPad]")
                                    .color(TextColor.color(0, 255, 0)) // green
                                    .append(Component.text(" Pad Set at: " + padX + ", " + padY + ", " + padZ)
                                            .color(TextColor.color(255, 255, 0))) // yellow
                                    .append(Component.text(" With Powers (x, y, z): " + padXPower + ", " + padYPower + ", " + padZPower)
                                            .color(TextColor.color(0, 191, 255)))); // light blue
                        }
                    }
                }
            }
        }

        return true;
    }
}