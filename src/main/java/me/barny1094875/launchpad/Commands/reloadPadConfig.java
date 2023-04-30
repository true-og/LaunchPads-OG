package me.barny1094875.launchpad.Commands;

import me.barny1094875.launchpad.LaunchPad;
import me.barny1094875.launchpad.config.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class reloadPadConfig implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(command.getName().equalsIgnoreCase("reloadpadconfig")) {
            if(sender instanceof Player){
                if(sender.hasPermission("launchpad.canreload")){
                    LaunchPad.config().reloadConfig();
                    // check through the config to make sure that
                    // there are as many launchpads as numberOfPads
                    // if there is not, move all of the pad number down
                    // by one starting at the missing number
                    Config padConfig = LaunchPad.config();
                    FileConfiguration padCoords = padConfig.getConfig();
                    int numberOfPads = padCoords.getInt("numberOfPads");

                    if(numberOfPads != 1) {
                        for (int i = 1; i < numberOfPads; i++) {
                            // if the world is null, then the pad is not set
                            if (padCoords.getString(i + ".world") == null) {
                                // move all other pads up one id number
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
                                    padCoords.set("" + j + ".searchID", padCoords.getString("" + (j + 1) + ".searchID"));
                                }
                                padCoords.set("" + numberOfPads, null);
                                padCoords.set("numberOfPads", --numberOfPads);
                                padConfig.save();
                            }
                        }
                        // check if the last pad exists
                        if(padCoords.getString(numberOfPads + ".world") == null){
                            // decrement numberOfPads by one
                            padCoords.set("numberOfPads", --numberOfPads);
                            padConfig.save();
                        }
                    }
                    else{
                        // if numberOfPads is 1, then there can only be one pad
                        // check if it exists. If not, decrement numberOfPads
                        System.out.println("checking");
                        if(padCoords.getString("1.world") == null){
                            padCoords.set("numberOfPads", 0);
                            padConfig.save();
                        }
                    }

                    // tell the player that ran the command that they plugin config has been reloaded
                    sender.sendMessage(Component.text("[LaunchPad]")
                            .color(TextColor.color(0, 255, 0))
                            .append(Component.text(" Config Reloaded")
                            .color(TextColor.color(255, 255, 0))));
                }
            }
        }

        return true;
    }
}
