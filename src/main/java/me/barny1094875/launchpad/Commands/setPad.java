package me.barny1094875.launchpad.Commands;

import me.barny1094875.launchpad.LaunchPad;
import me.barny1094875.launchpad.config.Config;
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

        if(command.getName().equalsIgnoreCase("setPad")) {
            // if the command is just /setpad
            // set a new pad to the player's location
            // and set the power to the default values
            if(args.length == 0) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    Location playerLocation = player.getLocation();

                    Config padConfig = LaunchPad.config();
                    FileConfiguration padCoords = padConfig.getConfig();
                    int padNumber = padCoords.getInt("numberOfPads") + 1;

                    padCoords.set(padNumber + ".world", player.getWorld().getName());
                    padCoords.set(padNumber + ".x", playerLocation.getBlockX());
                    padCoords.set(padNumber + ".y", playerLocation.getBlockY());
                    padCoords.set(padNumber + ".z", playerLocation.getBlockZ());
                    padCoords.set(padNumber + ".xpower", padCoords.getInt("defaultXpower"));
                    padCoords.set(padNumber + ".ypower", padCoords.getInt("defaultYpower"));
                    padCoords.set(padNumber + ".zpower", padCoords.getInt("defaultZpower"));
                    // this makes it easier to search the config file
                    // use CTRL+F and search for the coords of the pad
                    padCoords.set(padNumber + ".searchID", playerLocation.getBlockX() + " " + playerLocation.getBlockY() + " " + playerLocation.getBlockZ());

                    padCoords.set("numberOfPads", padNumber);

                    padConfig.save();
                }
            }
            // if the command is
            // /setpad <xpower> <ypower> <zpower>
            // set a new pad to the player's location
            // with the power values provided
            else if(args.length == 3){
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    Location playerLocation = player.getLocation();

                    Config padConfig = LaunchPad.config();
                    FileConfiguration padCoords = padConfig.getConfig();
                    int padNumber = padCoords.getInt("numberOfPads") + 1;

                    padCoords.set(padNumber + ".world", player.getWorld().getName());
                    padCoords.set(padNumber + ".x", playerLocation.getBlockX());
                    padCoords.set(padNumber + ".y", playerLocation.getBlockY());
                    padCoords.set(padNumber + ".z", playerLocation.getBlockZ());
                    padCoords.set(padNumber + ".xpower", Integer.valueOf(args[0]));
                    padCoords.set(padNumber + ".ypower", Integer.valueOf(args[1]));
                    padCoords.set(padNumber + ".zpower", Integer.valueOf(args[2]));
                    // this makes it easier to search the config file
                    // use CTRL+F and search for the coords of the pad
                    padCoords.set(padNumber + ".searchID", playerLocation.getBlockX() + " " + playerLocation.getBlockY() + " " + playerLocation.getBlockZ());

                    padCoords.set("numberOfPads", padNumber);

                    padConfig.save();
                }
            }
            // if the command is
            // /sepad <x> <y> <z> <xpower> <ypower> <zpower>
            // set a new pad to the provided location
            // in the player's world
            // with the power values provided
            else if(args.length == 6){
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    Location playerLocation = player.getLocation();

                    Config padConfig = LaunchPad.config();
                    FileConfiguration padCoords = padConfig.getConfig();
                    int padNumber = padCoords.getInt("numberOfPads") + 1;

                    padCoords.set(padNumber + ".world", player.getWorld().getName());
                    padCoords.set(padNumber + ".x", Integer.valueOf(args[0]));
                    padCoords.set(padNumber + ".y", Integer.valueOf(args[1]));
                    padCoords.set(padNumber + ".z", Integer.valueOf(args[2]));
                    padCoords.set(padNumber + ".xpower", Integer.valueOf(args[3]));
                    padCoords.set(padNumber + ".ypower", Integer.valueOf(args[4]));
                    padCoords.set(padNumber + ".zpower", Integer.valueOf(args[5]));
                    // this makes it easier to search the config file
                    // use CTRL+F and search for the coords of the pad
                    padCoords.set(padNumber + ".searchID", playerLocation.getBlockX() + " " + playerLocation.getBlockY() + " " + playerLocation.getBlockZ());

                    padCoords.set("numberOfPads", padNumber);

                    padConfig.save();
                }
            }
        }

        return true;
    }
}